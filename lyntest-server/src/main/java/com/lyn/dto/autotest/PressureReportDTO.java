package com.lyn.dto.autotest;

import com.lyn.vo.AutoTestResponseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2020/12/18
 */
@Data
public class PressureReportDTO {

    private String id;

    /**
     * 并发线程数
     */
    private Integer threads;

    /**
     * 重复执行次数
     */
    private Integer times;

    /**
     * httpCaseId
     */
    private Integer caseId;

    /**
     * 执行人
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

    private Integer passCount;

    private Integer errorCount;

    /**
     * 测试结果
     */
    private List<AutoTestResponseVO> result;

    /**
     * 平均响应时间
     */
    private Integer average;

    /**
     * 中位数
     */
    private Integer median;

    private Integer ninety;

    private Integer ninetyFive;

    private Integer ninetyNine;

    /**
     * 最小响应时间
     */
    private Integer min;

    /**
     * 最大响应时间
     */
    private Integer max;
}
