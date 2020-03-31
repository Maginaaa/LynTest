package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Data
public class HttpCaseList {

    private int page;

    private int pageSize;

    private HttpCase httpCase;
}
