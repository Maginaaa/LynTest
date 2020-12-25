package com.lyn.controller.track;


import com.lyn.common.util.PageUtil;
import com.lyn.dto.track.TestCaseSearchDTO;
import com.lyn.model.track.TestCaseDO;
import com.lyn.service.track.TestCaseService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;
import io.github.talelin.core.annotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

/**
* @author  简单随风    - 自动生成代码
* @since 2020-11-25
*/
@RestController
@RequestMapping("/case")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @PostMapping("/create")
    @LoginRequired
    public CreatedVO create(@RequestBody TestCaseDO testCaseDO) {
        testCaseService.createCase(testCaseDO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    @LoginRequired
    public UpdatedVO update(@RequestBody TestCaseDO testCaseDO) {
        testCaseService.editCase(testCaseDO);
        return new UpdatedVO();
    }

    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        testCaseService.deleteCaseById(id);
        return new DeletedVO();
    }

    @PostMapping("/list")
    public PageResponseVO<TestCaseDO> list(@RequestBody @Validated TestCaseSearchDTO testCaseSearchDTO) {
        return PageUtil.build(testCaseService.searchCaseList(testCaseSearchDTO));
    }

}
