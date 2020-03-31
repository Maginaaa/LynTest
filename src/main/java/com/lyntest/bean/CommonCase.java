package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/12/24
 */
@Data
public class CommonCase {

    /**
     * 前端传入的id
     * 不入库，在函数里转换为caseId
     */
    private Integer id;

    /** 集合id */
    private Integer collectionId;

    /** caseId */
    private Integer caseId;

    /** Case名称 */
    private String caseName;

    /**
     * 集合类型
     * 1:http 2:dubbo  3:mysql
     */
    private Integer caseType;

    private Object caseInfo;

}
