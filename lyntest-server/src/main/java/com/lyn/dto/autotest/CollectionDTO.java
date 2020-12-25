package com.lyn.dto.autotest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyn.common.validates.CronLength;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 简单随风
 * @date 2020/10/11
 */
@Data
public class CollectionDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 集合名称
     */
    @NotBlank
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
    private List<String> pushList;

    /**
     * 描述
     */
    private String description;

    /**
     * 定时任务
     */
    @CronLength
    private String cron;
    private Boolean timingSwitch;
}
