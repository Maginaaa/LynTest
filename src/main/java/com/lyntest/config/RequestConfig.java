package com.lyntest.config;

/**
 * @author 简单随风
 * @date 2020/1/16
 */
public class RequestConfig {
    /**
     * 配置okhttp链接超时时间
     * 单位 s
     */
    /** 链接超时 */
    public static int connectTimeout = 30;
    /** 写入超时 */
    public static int writeTimeout = 30;
    /** 读取超时 */
    public static int readTimeout = 30;
}
