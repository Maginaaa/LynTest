package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  @author 简单随风
 * @since 2020-09-10
 */
@Data
@Accessors(chain = true)
@TableName("api_category")
public class CategoryDO {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String categoryName;

    private String description;


}
