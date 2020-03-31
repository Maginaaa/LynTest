package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/12/17
 */
@Data
public class SqlCaseList {

    private int page;

    private int pageSize;

    private SqlCase sqlCase;
}
