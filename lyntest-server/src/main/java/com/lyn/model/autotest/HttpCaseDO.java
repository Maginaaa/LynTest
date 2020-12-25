package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *  @author 简单随风
 * @since 2020-09-04
 */
@Data
@TableName("api_http_case")
public class HttpCaseDO {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建人
     */
    private String creatorName;

    /**
     * 创建人code
     */
    private String creatorCode;

    /**
     * 创建日期
     */
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createDate;

    /**
     * 更新人
     */
    private String updaterName;

    /**
     * 更新人code
     */
    private String updaterCode;

    /**
     * 更新日期
     */
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date updateDate;

    /**
     * 分组
     */
    private String category;

    /**
     * 接口名称
     */
    private String caseName;

    /**
     * 接口url
     */
    private String apiUrl;

    /**
     * 请求方式
     */
    private String apiMethod;

    /**
     * 备注
     */
    private String description;

    /**
     * 请求头
     */
    private String headerValue;

    /**
     * body类型：1.json 2.urlform
     */
    private Integer bodyType;

    /**
     * body值
     */
    private String bodyValue;

    /**
     * 需要保存的变量
     */
    private String variableList;

    /**
     * 需要校验的参数
     */
    private String expectedList;


}
