package com.lyntest.config;

public class LoginConfig {
    /**
     * 用户登录
     */
    public static String RES_SUCCESS = "200";

    /** JWT TOKEN 过期时间为1天*/
    public static Long JWT_EXPIRES_SECOND = 86400000L;
//    public static Long JWT_EXPIRES_SECOND = 20000L;
    /** 用户头部入参标记 */
    public static final String TOKEN_HEADER = "Authorization";
    /**TOKEN 前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**参数缺失,无法解析 */
    public static final String HTTP_STATUS_401 = "401";
    /** 鉴权失败 */
    public static final String HTTP_STATUS_403 = "403";
    /** 鉴权异常 */
    public static final String HTTP_STATUS_404 = "404";
}
