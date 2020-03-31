package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Data
public class RequestUrlForm {

    /** urlform请求 key */
    private String formKey;

    /** urlform请求 value */
    private Object formValue;
}
