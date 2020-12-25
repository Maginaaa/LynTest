package com.lyn.model.track;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-25
 */
@Data
@Accessors(chain = true)
@TableName("case")
public class TestCaseDO {


    /**
     * 用例id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模块id
     */
    private Integer nodeId;

    /**
     * 所属项目id
     */
    private Integer projectId;

    /**
     * 用例名称
     */
    private String name;

    /**
     * 用例类型
     */
    private String type;

    /**
     * 用例优先级
     */
    private String priority;

    /**
     * 备注
     */
    private String description;

    /**
     * 执行步骤
     */
    private String steps;

    /**
     * 用例评审状态
     */
    private String reviewStatus;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建人工号
     */
    private String creatorCode;

    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后更新人姓名
     */
    private String updateName;

    /**
     * 最后更新人工号
     */
    private String updateCode;

    /**
     * 最后更新时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
