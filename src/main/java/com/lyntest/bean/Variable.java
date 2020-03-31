package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/10/12
 */
@Data
public class Variable {

    private int id;

    /** 集合id */
    private Integer collectionId;

    /** 变量名 */
    private String variableName;

    /** 变量值 */
    private String variableValue;

    /** 描述 */
    private String description;
}
