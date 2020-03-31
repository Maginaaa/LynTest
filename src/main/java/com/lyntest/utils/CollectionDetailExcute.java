package com.lyntest.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lyntest.bean.CommonCase;
import com.lyntest.bean.DataBase;
import com.lyntest.bean.DubboCase;
import com.lyntest.bean.HttpCase;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author 简单随风
 * @date 2019/9/25
 */
public class CollectionDetailExcute {


    private static Integer collectionId = 0;

    @DataProvider(name = "testData")
    private Iterator<Object[]> getData(){

        return new CaseDataProvider(ApiTestConfig.caseList);
    }


    @Test(dataProvider = "testData")
    public void testCase(CommonCase commonCase){

        Reporter.log(String.valueOf(Thread.currentThread().getId()));

        collectionId = ApiTestConfig.collectionId;

        Integer caseType = commonCase.getCaseType();

        Object caseInfo = commonCase.getCaseInfo();

        // caseType: 1:Http 2:Dubbo 3:Sql
        switch (caseType){
            case ApiTestConfig.HTTP_CASE_TYPE:
                httpExcute((HttpCase)caseInfo);
                break;
            case ApiTestConfig.DUBBO_CASE_TYPE:
                dubboExcute((DubboCase)caseInfo);
                break;
            case ApiTestConfig.SQL_CASE_TYPE:
                sqlExcute((DataBase)caseInfo);
                break;
            default:
                break;
        }

    }

    private void httpExcute(HttpCase httpCase){

        // 返回结果的body
        String result = "";

        Response response = ApiTestUtils.doHttpRequest(httpCase, collectionId);

        try {
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject resultObj = JSONObject.parseObject(result);
        String pretty = JSON.toJSONString(resultObj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        Reporter.log("接口返回结果为：\n" + pretty);

        // 保存变量
        ApiTestUtils.saveVariable(result, httpCase.getVariableListValue(), collectionId);

        // 执行校验
        ApiTestUtils.verifyResult(result, httpCase.getExpectedListValue(), collectionId);
    }


    private void dubboExcute(DubboCase dubboCase){

        String result =  ApiTestUtils.doDubboRequest(dubboCase, collectionId);

        Reporter.log("接口返回结果为：\n" + result);

        // 保存变量
        ApiTestUtils.saveVariable(result, dubboCase.getVariableListValue(), collectionId);

        // 执行校验
        ApiTestUtils.verifyResult(result, dubboCase.getExpectedListValue(), collectionId);

    }

    private void sqlExcute(DataBase dataBase){

        String result =  ApiTestUtils.doSqlRequest(dataBase);

        Reporter.log("接口返回结果为：\n" + result);

        // 保存变量
        ApiTestUtils.saveVariable(result, dataBase.getSqlCase().getVariableListValue(), collectionId);

        // sql执行默认全部执行完成
        Assert.assertTrue(Boolean.TRUE);

    }


}
