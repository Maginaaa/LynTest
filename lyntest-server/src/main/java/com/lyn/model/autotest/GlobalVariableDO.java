package com.lyn.model.autotest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  @author 简单随风
 * @since 2020-09-16
 */
@Data
@TableName("api_global_variable")
public class GlobalVariableDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String variableName;

    private String variableValue;

    private String description;

}
