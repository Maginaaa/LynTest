package com.lyntest.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/11/22
 */
@Data
public class DubboCase {

    /** 主键id */
    private int id;

    /** 创建人 */
    private String createrName;

    /** 创建人code */
    private String createrCode;

    /** 创建时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createDate;

    /** 更新人 */
    private String updaterName;

    /** 更新人 */
    private String updaterCode;

    /** 更新时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date updateDate;

    /** 所属系统名称 */
    private String systemName;

    /** 前端填写的caseName */
    private String caseName;

    /** 传给dubbo服务器的接口名 */
    private String apiName;

    /** zookeeper地址 */
    private String zkAddress;

    /** 服务名 */
    private String serviceName;

    /** 分组 */
    private String groupName;

    /** zookeeper版本 */
    private String version;

    /** 入参 */
    private String params;

    /** 函数名 */
    private String functionName;

    /** 类名 */
    private String className;

    /** 备注 */
    private String description;

    /** 需要保存的变量 */
    private String variableListValue;
    private List<VariableSave> variableList;

    /** 校验方式 */
    private String expectedListValue;
    private List<Expected> expectedList;

    /** 最后一次请求状态 */
    private Boolean status;

    /** 响应结果 */
    private String result;
}
