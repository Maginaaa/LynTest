package com.lyntest.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/17
 */
@Data
public class SqlCase {

    /** 主键id */
    private int id;

    /** 创建人 */
    private String createrName;

    /** 创建人code */
    private String createrCode;

    /** 创建时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createDate;

    /** case名 */
    private String caseName;

    /** 数据库名 */
    private String databaseName;

    /** 数据库id */
    private Integer databaseId;

    /** sql语句 */
    private String sql;

    /** 描述 */
    private String description;

    /** 需要保存的变量 */
    private String variableListValue;
    private List<VariableSave> variableList;

    /** 响应结果 */
    private String result;


}
