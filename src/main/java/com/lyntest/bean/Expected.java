package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Data
public class Expected {

    /**
     * 变量获取方式
     * 1.jsonPath  2.正则表达式
     */
    private int extractMethod;

    /** 提取路径/规则 */
    private String extractRule;

    /**
     * 校验方式
     * 1.equasl 2.contains
     */
    private int compareType;

    /**
     * 预期值
     */
    private String expectedValue;
}
