package com.lyntest.utils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jayway.jsonpath.JsonPath;
import com.lyntest.bean.*;
import com.lyntest.config.RequestConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.testng.Assert;
import org.testng.Reporter;

import java.sql.Connection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 简单随风
 * @date 2019/11/1
 */
@Slf4j
public class ApiTestUtils {


    /**
     *
     * @param httpCase 传入完整的case数据
     * @param collectionId 集合id，如果传入0则表示为接口调试执行
     * @return Response返回结果
     */
    public static Response doHttpRequest(HttpCase httpCase, Integer collectionId){

        String httpTypeValue = null;
        if (httpCase.getHttpType() == 1){
            httpTypeValue = "http://";
        } else if (httpCase.getHttpType() == 2){
            httpTypeValue = "https://";
        }
        String url = httpTypeValue + getVariable(httpCase.getApiUrl(), collectionId);

        if (null != httpCase.getApiPort()){
            url += ":" + String.valueOf(httpCase.getApiPort());
        }
        if (!httpCase.getApiPath().isEmpty()){
            url += getVariable(httpCase.getApiPath(),collectionId);
        }

        // 请求方式: POST/GET
        String apiMethod = httpCase.getApiMethod();

        // 请求类型：1.json  2.url form
        int bodyType = httpCase.getBodyType();
        String body = "";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(RequestConfig.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(RequestConfig.writeTimeout,TimeUnit.SECONDS)
                .readTimeout(RequestConfig.readTimeout, TimeUnit.SECONDS)
                .build();

        Request.Builder builder = new Request.Builder();

        // 设置header
        List<RequestHeaders> headersList = JSON.parseArray(httpCase.getHeaderValue(), RequestHeaders.class);
        for (RequestHeaders headers:headersList){
            builder.header(headers.getHeaderKey(), getVariable(headers.getHeaderValue(), collectionId));

        }

        // 设置body、mediaType
        String mediaTypeValue = "";
        if (bodyType == 1){
            mediaTypeValue = "application/json;charset=UTF-8";
            body = httpCase.getJsonValue();
        } else if (bodyType == 2){
            mediaTypeValue = "application/x-www-form-urlencoded;charset=utf-8";
            List<UrlFormBody> formBodyList = JSON.parseArray(httpCase.getFormValue(), UrlFormBody.class);
            StringBuffer sb = new StringBuffer();
            for (UrlFormBody formBody:formBodyList){
                sb.append(formBody.getFormKey()+"="+formBody.getFormValue()+"&");
            }
            body =  sb.toString();
        }

        // 如果前端没有传body，讲body设置为空string
        if (body == null){
            body = "";
        } else {
            body = getVariable(body, collectionId);
        }
        log.info("请求入参为：" + body);

        Request request = null;
        if (apiMethod.equals("POST")){
            RequestBody requestBody =RequestBody.create(MediaType.parse(mediaTypeValue), body);
            request = builder.url(url).post(requestBody).build();
        } else if(apiMethod.equals("GET")){
            request = builder.url(url).get().build();
        }

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
            if (e.toString().contains("Read timed out")){
                return null;
            }
        }

        return response;

    }

    /**
     * @param dubboCase 传入完整的case数据
     * @return Response返回结果
     */
    public static String doDubboRequest(DubboCase dubboCase, Integer collectionId){

        ApplicationConfig application = new ApplicationConfig();
        application.setName(dubboCase.getApiName());

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(getVariable(dubboCase.getZkAddress(),collectionId));

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(getVariable(dubboCase.getServiceName(),collectionId));
        // 声明为泛化接口
        reference.setGeneric(true);
        reference.setGroup(getVariable(dubboCase.getGroupName(),collectionId));
        reference.setVersion(getVariable(dubboCase.getVersion(),collectionId));
        try {
            ReferenceConfigCache cache = ReferenceConfigCache.getCache();
            GenericService genericService = cache.get(reference);

            HashMap<String, Object> maps = EntityUtil.jsonToMap(getVariable(dubboCase.getParams(),collectionId));

            // 基本类型以及Date,List,Map等不需要转换，直接调用
            Object result = genericService.$invoke(getVariable(dubboCase.getFunctionName(),collectionId),
                    new String[]{getVariable(dubboCase.getClassName(),collectionId)},
                    new Object[]{maps});
            return JSON.toJSONString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    public static String doSqlRequest(DataBase dataBase){

        String sql = dataBase.getSqlCase().getSql();
        String url = "";
        Integer databaseType = dataBase.getDatabaseType();
        if (databaseType == 1){
            url = "jdbc:mysql://" + dataBase.getUrl() + "?characterEncoding=UTF-8";
        }else if (databaseType == 2){
            url = "jdbc:oracle:thin:@" + dataBase.getUrl();
        }
        String username = dataBase.getUsername();
        String password = dataBase.getPassword();

        //  储存结果集
        JSONArray jsonArray = new JSONArray();

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            //  加载驱动
            if (databaseType == 1){
                Class.forName("com.mysql.jdbc.Driver");
            }else if (databaseType == 2){
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }
            //  获取数据库连接
            con = DriverManager.getConnection(url,username,password);
            st = con.prepareStatement(sql);

            //  通过正则进行增删改查的判断
            String selectReg = "^select|SELECT";
            Pattern selectP = Pattern.compile(selectReg);
            Matcher selectM = selectP.matcher(sql);

            //  查询
            if (selectM.find()){
                rs = st.executeQuery();
                //  获取查询结果元数据,如果rs为空则抛错，如果不为空则转为map赋值后转为String返回给前端
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();

                while (rs.next()){
                    JSONObject jsonObject = new JSONObject();
                    for (int i = 1; i <= columnCount; i++) {
                        jsonObject.put(md.getColumnName(i), rs.getObject(i));
                    }
                    jsonArray.add(jsonObject);
                }

                if (jsonArray.size()==0){
                    return "请检查sql语句";
                }

                return JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
            }
            //  改\查\删
            else {
                int i = st.executeUpdate();
                if(i > 0){
                    return "执行成功";
                }else {
                    return "执行失败，请检查sql语句";
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "数据库驱动加载失败";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    log.error("释放结果集失败");
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    log.debug("数据库链接关闭成功！");
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    log.error("关闭链接失败");
                }
            }
        }
    }

    /**
     *
     * @param string 变量名
     * @param collectionId 集合id，：为0时只读全局变量，非0时读了全局变量再读集合内变量
     * @return 变量值
     */
    private static String getVariable(String string,Integer collectionId){

        HashMap<String,String> variableMap = ApiTestConfig.globalVariableMap;

        // 如果为集合执行，会再次获取集合内变量
        if (collectionId != 0){
            variableMap.putAll(ApiTestConfig.collectionVariableMap);
        }

        // 查询string中是否有${KEY}格式的数据，如果有则将其替换为VALUE
        if (!string.isEmpty()){
            String reg = "\\$\\{.*?}";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(string);
            // 遍历替换所有的变量
            while (m.find()){
                String key = m.group().replace("${","").replace("}","");
                // 执行函数助手
                if (key.startsWith("__")){
                    String value = functionAssistant(key);
                    string = string.replace(key,value);
                } else if (variableMap.containsKey(key)){
                    // 执行普通变量替换
                    string = string.replace(m.group(),variableMap.get(key));
                }
            }
            return string;
        } else {
            return "";
        }

    }

    /** 保存变量 */
    public static void saveVariable(String result, String variableListValue, Integer collectionId) {
        if (result == null){
            return;
        }

        if (!result.isEmpty()) {
            List<VariableSave> variableList = JSON.parseArray(variableListValue, VariableSave.class);
            for (VariableSave variable : variableList) {
                // 提取方式：1.jsonPath  2.正则表达式
                int extractMethod = variable.getExtractMethod();
                String variableName = variable.getVariableName();
                String extractRule = variable.getExtractRule();
                // 提取到的结果
                String extractRes = "";
                if (extractMethod == 1) {
                    try {
                        Object o = JsonPath.read(result, extractRule);
                        extractRes = String.valueOf(o);
                    } catch (Exception e) {
                        log.error(String.valueOf(e));
                    }
                } else if (extractMethod == 2) {
                    Pattern p = Pattern.compile(extractRule);
                    Matcher m = p.matcher(result);
                    if (m.find()) {
                        extractRes = m.group();
                    }
                }
                // 如果是简单的调试执行则将变量保存到全局变量map中，如果是集合执行，则只保存到集合变量map中
                if (collectionId == 0) {
                    ApiTestConfig.globalVariableMap.put(variableName, extractRes);
                } else {
                    ApiTestConfig.collectionVariableMap.put(variableName, extractRes);
                }

            }
        }
    }

    /**
     * 遍历ExpectedList，只要有校验不通过的条件测抛FALSE
     */
    public static Boolean verifyResult(String responseResult, String expectedListValue, Integer collectionId){

        List<Expected> ExpectedList = JSON.parseArray(expectedListValue, Expected.class);
        if (ExpectedList.size() == 0){
            if (collectionId != 0){
                Assert.assertTrue(Boolean.TRUE);
            }
            return Boolean.TRUE;
        }

        for (Expected exp:ExpectedList){
            // 提取方式：1.jsonPath  2.正则表达式
            int extractMethod = exp.getExtractMethod();
            String extractRule = exp.getExtractRule();
            // 校验方式：1.equasl 2.contains
            int compareType = exp.getCompareType();
            String expectedValue = exp.getExpectedValue();

            // 实际获取结果
            String actualRes = "";
            if (extractMethod == 1){
                try {
                    // 这里取到的值如果是bool时，强转会抛错，所以需要用object接一下，再转String
                    Object o = JsonPath.read(responseResult, extractRule);
                    actualRes = String.valueOf(o);
                } catch (Exception e){
                    if (collectionId != 0){
                        Reporter.log("接口返回结果为：" + responseResult);
                        Assert.fail("预期值取值失败");
                    }

                    return Boolean.FALSE;
                }
            } else if (extractMethod == 2){
                try {
                    Pattern p = Pattern.compile(extractRule);
                    Matcher m = p.matcher(responseResult);
                    if (m.find()){
                        actualRes = m.group(0);
                    }
                } catch (Exception e){
                    if (collectionId != 0){
                        Assert.fail("预期值取值异常");
                    }
                    return Boolean.FALSE;
                }
            }

            // 对比预期结果
            if (compareType == 1){
                if (collectionId != 0){
                    // 集合执行，走testNg校验
                    Assert.assertEquals(actualRes, expectedValue);
                } else {
                    // 调试执行，走代码校验
                    if (!actualRes.equals(expectedValue)){
                        return Boolean.FALSE;
                    }
                }
            } else if (compareType == 2){
                if (collectionId != 0){
                    Assert.assertTrue(actualRes.contains(expectedValue));
                } else {
                    if (!actualRes.contains(expectedValue)){
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

    private static final String RANDOM = "__Random";
    private static final String TIME = "__time";

    /** 函数助手 */
    private static String functionAssistant(String key){

        /**
         * 入参：最小值，最大值，变量名
         * __Random(1,99,INT)   =>   从1~99的数字，保存变量为 INT
         */
        if (key.startsWith(RANDOM)){
            String parameters = key.replace(RANDOM + "(","").replace(")","");
            String[] paramList = parameters.split(",");
            // 格式不对，返回空string
            if (paramList.length != 3 && paramList.length != 2){
                return key;
            }
            Long min = Long.valueOf(paramList[0]);
            Long max = Long.valueOf(paramList[1]);
            String value = String.valueOf((long) (min + Math.random()*(max - min +1)));
            if (paramList.length == 3){
                ApiTestConfig.globalVariableMap.put(paramList[2], value);
            }
            return value;
        }

        /**
         * 入参：时间格式，变量名
         * __time(,)  获取当前时间毫秒
         * __time(/1000,)  获取当前时间秒
         * __time(yyyy-MM-dd,)  获取yyyy-MM-dd格式时间
         */
        if (key.startsWith(TIME)){
            String parameters = key.replace(TIME + "(","").replace(")","");
            String[] paramList = parameters.split(",");
            Long now = System.currentTimeMillis();
            if (paramList.length == 0){
                return String.valueOf(now);
            }
            if (paramList[0].isEmpty() && !paramList[1].isEmpty()){
                // __time(,Var)
                ApiTestConfig.globalVariableMap.put(paramList[1], String.valueOf(now));
            }
            if (paramList[0].equals("/1000")){
                String value = String.valueOf(now / 1000);
                // __time(/1000,Var)或 __time(/1000,)
                if (paramList.length == 2){
                    ApiTestConfig.globalVariableMap.put(paramList[1], value);
                }
                return value;
            }
            // __time(yyyy-MM-dd,)或__time(yyyy-MM-dd,Var)，包含其他各种日期格式
            SimpleDateFormat sdf = new SimpleDateFormat(paramList[0]);
            String value = sdf.format(new Date(now));
            if (paramList.length == 2){
                ApiTestConfig.globalVariableMap.put(paramList[1], value);
            }
            return value;

        }

        return key;


    }



}
