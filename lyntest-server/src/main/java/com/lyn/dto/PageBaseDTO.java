package com.lyn.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 简单随风
 * @date 2020/8/13
 */
@Data
public class PageBaseDTO {

    @Min(value = 1, message = "{page.count.min}")
    @Max(value = 100, message = "{page.count.max}")
    @NotNull
    private Integer count;

    @Min(value = 0, message = "{page.number.min}")
    @NotNull
    private Integer page;
}
