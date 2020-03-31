package com.lyntest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyntest.bean.*;
import com.lyntest.filter.HttpBasicAuthorizeAttribute;
import com.lyntest.mapper.ApiTestConfigMapper;
import com.lyntest.mapper.HttpCaseMapper;
import com.lyntest.service.HttpCaseManageService;
import com.lyntest.utils.ApiTestConfig;
import com.lyntest.utils.ApiTestUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Slf4j
@Service(value = "CaseManageService")
public class HttpCaseManageServiceImpl implements HttpCaseManageService {


    @Autowired
    private HttpCaseMapper httpCaseMapper;

    @Autowired
    private ApiTestConfigMapper apiTestConfigMapper;


    @Override
    public ResponseVo searchCreaterList() {

        ResponseVo responseVo = new ResponseVo();
        List<HttpCase> list = httpCaseMapper.selectCreaterList();

        responseVo.setResult(list);
        return responseVo;
    }

    @Override
    public ResponseVo searchHttpCaseList(HttpCaseList httpCaseList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(httpCaseList.getPage(), httpCaseList.getPageSize());

        List<HttpCase> list = httpCaseMapper.selectHttpCaseList(httpCaseList.getHttpCase());

        // 遍历list，给header，formValue,variableList,expectedList赋值
        for (HttpCase httpCase :list){
            if (!httpCase.getHeaderValue().isEmpty()){
                setJsonValue(httpCase);
            }
        }

        PageInfo pageInfo = new PageInfo<>(list);

        responseVo.setResult(pageInfo);

        return responseVo;
    }

    @Override
    public ResponseVo createHttpCase(HttpCase httpCase) {

        ResponseVo responseVo = new ResponseVo();

        // 添加创建人信息
        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        httpCase.setCreaterCode(u.getCode());
        httpCase.setCreaterName(u.getName());
        httpCase.setCreateDate(new Date());

        // 最后更新人的信息清空
        httpCase.setUpdateDate(null);
        httpCase.setUpdaterCode(null);
        httpCase.setUpdaterName(null);

        // 给headerValue，formValue,variableListValue,expectedListValue赋值
        setValueForJson(httpCase);

        // 执行状态置为失败
        httpCase.setStatus(Boolean.FALSE);

        int i = httpCaseMapper.createHttpCase(httpCase);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }

        return responseVo;
    }


    @Override
    public ResponseVo updateHttpCase(HttpCase httpCase) {

        ResponseVo responseVo = new ResponseVo();

        // 添加修改人信息
        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        httpCase.setUpdaterCode(u.getCode());
        httpCase.setUpdaterName(u.getName());
        httpCase.setUpdateDate(new Date());

        // 给headerValue，formValue,variableListValue,expectedListValue赋值
        setValueForJson(httpCase);

        int i = httpCaseMapper.updateHttpCase(httpCase);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo deleteHttpCase(Integer[] ids) {

        ResponseVo responseVo = new ResponseVo();

        int i = httpCaseMapper.deleteHttpCase(ids);
        if (i==ids.length){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }

        return responseVo;
    }

    /**
     * case的执行
     * @param ids
     * @return
     */
    @Override
    public ResponseVo excuteRequest(Integer[] ids) {

        ResponseVo responseVo = new ResponseVo();

        // 全局变量赋值
        List<Variable> variableList = apiTestConfigMapper.selectGlobalVariable();
        for (Variable variable:variableList){
            ApiTestConfig.globalVariableMap.put(variable.getVariableName(), variable.getVariableValue());
        }

        // 获取当前选中的所有case
        List<HttpCase> caseList = httpCaseMapper.selectHttpCaseListByIds(ids);
        // 遍历caseList，进行http请求
        for (HttpCase httpCase :caseList){
            // 保存响应结果
            String result = "";

            try {
                Response response = ApiTestUtils.doHttpRequest(httpCase, ApiTestConfig.GLOBAL_COLLECTION_ID);
                result = response.body().string();
            } catch (Exception e) {
                // 将执行后的结果返回给前端
                result = "接口响应异常";
                e.printStackTrace();
            }

            // 保存变量
            ApiTestUtils.saveVariable(result, httpCase.getVariableListValue(), ApiTestConfig.GLOBAL_COLLECTION_ID);

            // 判断是否通过了所有校验条件
            if (ApiTestUtils.verifyResult(result, httpCase.getExpectedListValue(), ApiTestConfig.GLOBAL_COLLECTION_ID)){
                httpCase.setStatus(Boolean.TRUE);
            } else {
                httpCase.setStatus(Boolean.FALSE);
            }

            try {
                // Json样式美化
                JSON resultJson = JSON.parseObject(result);
                String pretty = JSON.toJSONString(resultJson, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
                // 给testCase的执行状态进行赋值
                httpCase.setResult(pretty);
            } catch (Exception e){
                httpCase.setResult(result);
            }


            // 更新数据库保存的信息
            setJsonValue(httpCase);
            httpCaseMapper.updateHttpCase(httpCase);

            // 将执行后的结果返回给前端
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult(httpCase);
        }

        return responseVo;
    }

    /**
     * 给header，formValue,variableList,expectedList赋值
     */
    private void setJsonValue(HttpCase httpCase){
        httpCase.setHeaderForm(JSON.parseArray(httpCase.getHeaderValue(), RequestHeaders.class));
        httpCase.setFormBody(JSON.parseArray(httpCase.getFormValue(), UrlFormBody.class));
        httpCase.setVariableList(JSON.parseArray(httpCase.getVariableListValue(), VariableSave.class));
        httpCase.setExpectedList(JSON.parseArray(httpCase.getExpectedListValue(), Expected.class));
    }

    /**
     * 给headerValue，formValue,variableListValue,expectedListValue赋值
     */
    private void setValueForJson(HttpCase httpCase){
        httpCase.setHeaderValue(JSON.toJSONString(httpCase.getHeaderForm()));
        httpCase.setFormValue(JSON.toJSONString(httpCase.getFormBody()));
        httpCase.setVariableListValue(JSON.toJSONString(httpCase.getVariableList()));
        httpCase.setExpectedListValue(JSON.toJSONString(httpCase.getExpectedList()));
    }

}