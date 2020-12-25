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
@TableName("api_pocket_case")
public class PocketCaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 包id
     */
    private Integer pocketId;

    /**
     * case id
     */
    private Integer caseId;

    /**
     * case名
     */
    private String caseName;

    private Integer pos;
}
