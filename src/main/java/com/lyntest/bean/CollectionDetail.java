package com.lyntest.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/24
 */
@Data
public class CollectionDetail {

    private int id;

    /** 是否关注 */
    private Boolean isFocus;

    /** 集合名 */
    private String collectionName;

    /** 是否使用微信推送测试报告 */
    private Boolean wxPush;

    /** 创建人名 */
    private String createrName;

    /** 创建人code */
    private String createrCode;

    /** 最后更新人名 */
    private String excuterName;

    /** 最后更新人code */
    private String excuterCode;

    /** 最后更新时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date excuteDatetime;

    /** 线程数 */
    private Integer threadPoolSize;

    /** 重复执行次数 */
    private Integer repeatTimes;

    /** 变量 */
    private List<Variable> variableList;

    /** 标签 */
    private String tagList;

    /** 测试报告路径 */
    private String reportPath;

    /** 开始时间(用于搜索) */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /** 结束时间(用于搜索) */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;

}
