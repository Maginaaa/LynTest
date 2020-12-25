package com.lyn.model.track;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author  简单随风
 * @date 2020/12/14
 */
@Data
@Accessors(chain = true)
@Builder
@TableName("case_reviewer")
public class TestCaseReviewerDO {

    private String userCode;

    private Integer reviewId;
}
