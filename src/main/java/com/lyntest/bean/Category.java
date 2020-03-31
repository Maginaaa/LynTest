package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/10/21
 */
@Data
public class Category {

    private int id;

    // 类别名称
    private String categoryName;

    // 描述
    private String description;
}
