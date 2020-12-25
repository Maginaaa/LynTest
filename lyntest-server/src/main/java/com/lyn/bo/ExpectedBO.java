package com.lyn.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 简单随风
 * @date 2020/9/8
 */
@Data
public class ExpectedBO {

    private Boolean enable;

    /**
     * 变量获取方式
     * 1.jsonPath  2.正则表达式
     */
    @NotNull(message = "{autotest.assertions.method.not-null}")
    private Integer extractMethod;

    /** 提取路径/规则 */
    @NotBlank(message = "{autotest.assertions.rule.not-blank}")
    private String extractRule;

    /**
     * 校验方式
     * 1.equasl 2.contains
     */
    @NotNull(message = "{autotest.assertions.type.not-null}")
    private Integer compareType;

    /**
     * 预期值
     */
    @NotBlank(message = "{autotest.assertions.expected-value.not-blank}")
    private String expectedValue;

    /**
     * 真实值
     */
    private String actualValue;

    /**
     * 校验是否通过
     */
    private Boolean extractSuccess;
}
