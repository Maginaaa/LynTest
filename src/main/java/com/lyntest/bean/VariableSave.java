package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Data
public class VariableSave {

    /**
     * 变量保存方式
     * 1.jsonPath  2.正则表达式
     */
    private int extractMethod;

    /** 提取路径/规则 */
    private String extractRule;

    /** 变量名 */
    private String variableName;
}
