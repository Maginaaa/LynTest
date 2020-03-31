package com.lyntest.utils;

import com.lyntest.bean.CommonCase;

import java.util.HashMap;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/9
 */
public class ApiTestConfig {

    /** 全局变量 */
    public static HashMap<String,String> globalVariableMap = new HashMap<>();

    /** 集合id */
    public static Integer collectionId = null;

    /** 集合变量 */
    public static HashMap<String,String> collectionVariableMap = new HashMap<>();

    /** 保存集合的apiList */
    public static List<CommonCase> caseList;

    /** 测试报告名 */
    public static String reportName = "";

    /** 全局执行(非集合内执行)时的collectionId */
    public static final int GLOBAL_COLLECTION_ID = 0;

    /** 线程重复次数 */
    public static Integer repeatTimes = 1;
    public static Integer threadPoolSize = 1;

    /** 集合内的case类型 */
    public static final int HTTP_CASE_TYPE = 1;
    public static final int DUBBO_CASE_TYPE = 2;
    public static final int SQL_CASE_TYPE = 3;



}
