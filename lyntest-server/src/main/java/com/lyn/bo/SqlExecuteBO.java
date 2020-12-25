package com.lyn.bo;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2020/8/4
 */
@Data
public class SqlExecuteBO {

    private String url;

    private String username;

    private String password;

    private String sql;

}
