package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/12/16
 */
@Data
public class DataBaseList {

    private int page;

    private int pageSize;

    private DataBase dataBase;
}
