package com.lyn.bo;

import lombok.Data;

import java.util.List;

/**
 * @author 简单随风
 * @date 2020/9/24
 */
@Data
public class TreeBO {

    private Integer id;

    private String name;

    private Integer businessId;

    /**
     * 树层级
     */
    private Integer level;

    /**
     * position：用于节点排序
     */
    private Integer pos;

    private List<TreeBO> children;
}
