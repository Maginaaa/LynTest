package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  @author 简单随风
 * @since 2020-09-18
 */
@Data
@TableName("api_collection")
public class CollectionDO {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 集合名称
     */
    private String collectionName;

    /**
     * 创建人名
     */
    private String creatorName;

    /**
     * 创建人工号
     */
    private String creatorCode;

    /**
     * 推送类型
     * 1.不推送 2.失败时推送 3.总是推送
     */
    private Integer pushType;

    /**
     * 推送人列表
     */
    private String pushList;

    /**
     * 描述
     */
    private String description;

    /**
     * 定时任务
     */
    private String cron;
    private Boolean timingSwitch;


}
