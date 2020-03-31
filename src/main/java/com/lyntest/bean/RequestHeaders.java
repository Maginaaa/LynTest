package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Data
public class RequestHeaders{

    /** 请求头 key */
    private String headerKey;

    /** 请求头 value */
    private String headerValue;

}
