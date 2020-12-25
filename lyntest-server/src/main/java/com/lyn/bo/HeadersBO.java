package com.lyn.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 简单随风
 * @date 2020/9/8
 */
@Data
public class HeadersBO {

    private Boolean enable;

    /** 请求头 key */
    @NotBlank(message = "{autotest.header.key.not-blank}")
    private String headerKey;

    /** 请求头 value */
    @NotBlank(message = "{autotest.header.value.not-blank}")
    private String headerValue;
}
