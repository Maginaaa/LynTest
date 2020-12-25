package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author 简单随风
 * @date 2020/10/9
 */
@Data
@TableName("api_collection_report")
public class CollectionReportDO {

    private String id;

    private String collectionName;

    /**
     * 集合id
     */
    private Integer collectionId;

    /**
     * 执行人姓名
     */
    private String executeName;

    /**
     * 执行人工号
     */
    private String executeCode;

    /**
     * 执行时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    /**
     * 通过和失败
     */
    private Integer passCount;
    private Integer errorCount;

    /**
     * 测试结果
     */
    @JsonIgnore
    private String result;
}
