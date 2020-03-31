package com.lyntest.bean;

import lombok.Data;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/30
 */
@Data
public class CollectionCaseList {

    /** 集合id */
    private Integer collectionId;


    /** 接口详情 */
    private List<CommonCase> caseList;
}
