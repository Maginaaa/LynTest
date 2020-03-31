package com.lyntest.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Data
public class HttpCase {

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

    /** 接口名称 */
    private String caseName;

    /** 1:http 2:https */
    private Integer httpType;

    /** url */
    private String apiUrl;

    /** 端口号 */
    private String apiPort;

    /** 请求方式 */
    private String apiMethod;

    /** 请求路径 */
    private String apiPath;

    /** 备注 */
    private String description;

    /** headers */
    private String headerValue;
    private List<RequestHeaders> headerForm;

    /**
     * body类型
     * 1.json  2.url form
     */
    private int bodyType;

    /** body-json */
    private String jsonValue;

    /** body-urlform */
    private String formValue;
    private List<UrlFormBody> formBody;

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
