package com.lyn.controller.track;


import com.lyn.common.util.PageUtil;
import com.lyn.dto.track.ReviewSearchDTO;
import com.lyn.dto.track.TestCaseReviewDTO;
import com.lyn.model.track.TestCaseReviewDO;
import com.lyn.service.track.TestCaseReviewService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;
import io.github.talelin.core.annotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

/**
* @author  简单随风    - 自动生成代码
* @since 2020-12-14
*/
@RestController
@RequestMapping("/review")
public class TestCaseReviewController {

    @Autowired
    private TestCaseReviewService testCaseReviewService;

    @PostMapping("/create")
    @LoginRequired
    public CreatedVO create(@RequestBody TestCaseReviewDTO testCaseReviewDTO) {
        testCaseReviewService.createTestCaseReview(testCaseReviewDTO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody TestCaseReviewDTO testCaseReviewDTO) {
        testCaseReviewService.editTestCaseReview(testCaseReviewDTO);
        return new UpdatedVO();
    }

    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {

        return new DeletedVO();
    }

    @PostMapping("/list")
    public PageResponseVO<TestCaseReviewDO> list(@RequestBody ReviewSearchDTO reviewSearchDTO) {

        return PageUtil.build(testCaseReviewService.searchReviewList(reviewSearchDTO));
    }

}
