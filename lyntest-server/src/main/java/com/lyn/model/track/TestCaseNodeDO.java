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
 * @since 2020-11-23
 */
@Data
@Accessors(chain = true)
@TableName("case_node")
public class TestCaseNodeDO {

    /**
     * 节点id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * Node name
     */
    private String name;

    /**
     * 父节点id
     */
    private Integer parentId;

    /**
     * 节点level
     */
    private Integer level;

    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 排序
     */
    private Integer pos;

}
