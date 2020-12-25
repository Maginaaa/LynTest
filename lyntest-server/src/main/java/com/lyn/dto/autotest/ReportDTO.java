package com.lyn.dto.autotest;

import com.lyn.bo.VariableSaveBO;
import com.lyn.vo.AutoTestResponseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2020/10/10
 */
@Data
public class ReportDTO {

    private String id;

    /**
     * 集合id
     */
    private Integer collectionName;

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
    private List<AutoTestResponseVO> result;

}
