package com.lyntest.bean;


import lombok.Data;

@Data
public class User {

    /** 用户工号 */
    private String code;

    /** 姓名 */
    private String name;

    /** 密码 */
    private String password;
}

