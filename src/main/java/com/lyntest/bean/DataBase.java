package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/12/16
 */
@Data
public class DataBase {

    private Integer id;

    private String dbName;

    private String url;

    private String username;

    private String password;

    /** sql语句，只做执行使用 */
    private SqlCase sqlCase;

    private String description;

    /**
     * 数据库类型
     * 1：mysql  2：oracle
     */
    private Integer databaseType;
}

