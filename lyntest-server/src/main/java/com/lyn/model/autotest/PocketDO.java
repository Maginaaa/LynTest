package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 简单随风
 * @date 2020/9/24
 */
@Data
@TableName("api_pocket")
public class PocketDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 集合id
     */
    private Integer collectionId;

    /**
     * 包名
     */
    private String pocketName;

    private Integer pos;

}
