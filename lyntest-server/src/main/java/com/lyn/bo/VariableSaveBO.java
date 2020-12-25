package com.lyn.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 简单随风
 * @date 2020/9/8
 */
@Data
public class VariableSaveBO {

    private Boolean enable;

    /**
     * 变量保存方式
     * 1.jsonPath  2.正则表达式
     */
    @NotNull(message = "{autotest.variable.method.not-null}")
    private Integer extractMethod;

    /** 提取路径/规则 */
    @NotBlank(message = "{autotest.variable.rule.not-blank}")
    private String extractRule;

    /** 变量名 */
    @NotBlank(message = "{autotest.variable.variable-name.not-blank}")
    private String variableName;

    private String actualRes;
}
