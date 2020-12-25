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
 * @since 2020-12-14
 */
@Data
@Accessors(chain = true)
@TableName("case_review")
public class TestCaseReviewDO {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String creator;

    private String status;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String description;


}
