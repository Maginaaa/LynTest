package com.lyn.async;

import java.util.HashMap;

/**
 * @author 简单随风
 * @date 2020/9/11
 */
public class AutoTestConfig {

    /** 链接超时 */
    public static int connectTimeout = 30;
    /** 写入超时 */
    public static int writeTimeout = 30;
    /** 读取超时 */
    public static int readTimeout = 30;

    /** 全局变量 */
    public static HashMap<String,String> variableMap = new HashMap<>();
}
