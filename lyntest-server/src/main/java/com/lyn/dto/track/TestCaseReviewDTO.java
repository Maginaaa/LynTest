package com.lyn.dto.track;

import com.lyn.model.track.TestCaseReviewDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author  简单随风
 * @date 2020/12/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestCaseReviewDTO extends TestCaseReviewDO {

    private List<String> reviewer;

    private List<Integer> project;
}
