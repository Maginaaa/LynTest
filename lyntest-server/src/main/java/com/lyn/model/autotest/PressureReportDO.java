package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *  @author 简单随风
 * @since 2020-12-23
 */
@Data
@TableName("api_pressure_report")
public class PressureReportDO {

    private String id;

    /**
     * httpCase id
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
    private String result;

    /**
     * 线程数
     */
    private Integer threads;

    /**
     * 执行次数
     */
    private Integer times;

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
