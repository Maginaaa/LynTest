package com.lyn.async;

import com.lyn.bo.HeadersBO;
import com.lyn.bo.ResponseBO;
import com.lyn.dto.autotest.HttpCaseDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 简单随风
 * @date 2020/9/11
 */
@Slf4j
public class ApiRequest {

    /**
     *
     * @param httpCaseDTO 传入完整的case数据
     * @return Response返回结果
     */
    public static ResponseBO doHttpRequest(HttpCaseDTO httpCaseDTO){

        String url = httpCaseDTO.getApiUrl();

        // 请求方式: POST/GET
        String apiMethod = httpCaseDTO.getApiMethod();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(AutoTestConfig.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(AutoTestConfig.writeTimeout,TimeUnit.SECONDS)
                .readTimeout(AutoTestConfig.readTimeout, TimeUnit.SECONDS)
                .build();

        Request.Builder builder = new Request.Builder();

        // 设置header
        List<HeadersBO> headersList = httpCaseDTO.getHeaderForm();
        for (HeadersBO headers:headersList){
            if (headers.getEnable()){
                if (StringUtils.isEmpty(headers.getHeaderKey()) || StringUtils.isEmpty(headers.getHeaderValue())){
                    continue;
                }
                builder.header(headers.getHeaderKey(), headers.getHeaderValue());
            }
        }

        // 请求类型：1.json  2.url form
        Integer bodyType = httpCaseDTO.getBodyType();
        String mediaTypeValue = "";

        // 设置body、mediaType
        String body = httpCaseDTO.getBodyValue();
        log.info("请求入参为：" + body);

        // 设置请求content-type
        if (bodyType == 1){
            mediaTypeValue = "application/json;charset=UTF-8";
        } else if (bodyType == 2){
            mediaTypeValue = "application/x-www-form-urlencoded;charset=utf-8";
        }

        Request request = null;
        if ("POST".equals(apiMethod)){
            RequestBody requestBody =RequestBody.create(MediaType.parse(mediaTypeValue), body);
            request = builder.url(url).post(requestBody).build();
        } else if("GET".equals(apiMethod)){
            request = builder.url(url).get().build();
        }

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            return ResponseBO.failure(e.toString());
        }

        return ResponseBO.success(response);

    }

    /**
     * 替换所有变量
     * @param httpCaseDTO
     * @return
     */
    public static HttpCaseDTO variableReplace(HttpCaseDTO httpCaseDTO){

        // url
        httpCaseDTO.setApiUrl(getVariable(httpCaseDTO.getApiUrl()));
        // 请求文本
        String body = httpCaseDTO.getBodyValue();
        httpCaseDTO.setBodyValue(body != null ? getVariable(body) : "");
        // 请求头
        httpCaseDTO.getHeaderForm().forEach( h -> {
            if (h.getEnable()) {
                h.setHeaderKey(getVariable(h.getHeaderKey()));
                h.setHeaderValue(getVariable(h.getHeaderValue()));
            }
        });
        // 断言
        httpCaseDTO.getExpectedList().forEach( e -> {
            if (e.getEnable()) {
                e.setExpectedValue(getVariable(e.getExpectedValue()));
            }
        });
        return httpCaseDTO;



    }


    /**
     * @param key 需要替换变量String
     * @return 变量值
     */
    private static String getVariable(String key){


        if (StringUtils.isEmpty(key)){
            return "";
        }

        HashMap<String,String> variableMap = AutoTestConfig.variableMap;

        // 查询string中是否有${KEY}格式的数据，如果有则将其替换为VALUE
        String reg = "\\$\\{.*?}";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(key);
        // 遍历替换所有的变量
        while (m.find()){
            String keyNew = m.group().replace("${","").replace("}","");
            // 执行函数助手
            if (keyNew.startsWith("__")){
                String value = functionAssistant(keyNew);
                key = key.replace(keyNew,value);
            } else if (variableMap.containsKey(keyNew)){
                // 执行普通变量替换
                key = key.replace(m.group(),variableMap.get(keyNew));
            }
        }
        return key;
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
            long min = Long.parseLong(paramList[0]);
            long max = Long.parseLong(paramList[1]);
            String value = String.valueOf((long) (min + Math.random()*(max - min +1)));
            if (paramList.length == 3){
                AutoTestConfig.variableMap.put(paramList[2], value);
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
            long now = System.currentTimeMillis();
            if (paramList.length == 0){
                return String.valueOf(now);
            }
            if (paramList[0].isEmpty() && !paramList[1].isEmpty()){
                // __time(,Var)
                AutoTestConfig.variableMap.put(paramList[1], String.valueOf(now));
            }
            if ("/1000".equals(paramList[0])){
                String value = String.valueOf(now / 1000);
                // __time(/1000,Var)或 __time(/1000,)
                if (paramList.length == 2){
                    AutoTestConfig.variableMap.put(paramList[1], value);
                }
                return value;
            }
            // __time(yyyy-MM-dd,)或__time(yyyy-MM-dd,Var)，包含其他各种日期格式
            SimpleDateFormat sdf = new SimpleDateFormat(paramList[0]);
            String value = sdf.format(new Date(now));
            if (paramList.length == 2){
                AutoTestConfig.variableMap.put(paramList[1], value);
            }
            return value;
        }
        return key;
    }
}
