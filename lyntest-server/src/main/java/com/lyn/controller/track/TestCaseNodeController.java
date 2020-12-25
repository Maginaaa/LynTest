package com.lyn.controller.track;


import com.lyn.bo.TreeBO;
import com.lyn.model.track.TestCaseNodeDO;
import com.lyn.service.track.TestCaseNodeService;
import com.lyn.service.track.TestCaseService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
* @author  简单随风    - 自动生成代码
* @since 2020-11-23
*/
@RestController
@RequestMapping("/node")
public class TestCaseNodeController {

    @Autowired
    private TestCaseNodeService testCaseNodeService;

    @Autowired
    private TestCaseService testCaseService;

    @PostMapping("/create")
    public CreatedVO create(@RequestBody TestCaseNodeDO testCaseNodeDO) {
        testCaseNodeService.addNode(testCaseNodeDO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody TestCaseNodeDO testCaseNodeDO) {
        testCaseNodeService.updateNode(testCaseNodeDO);
        return new UpdatedVO();
    }

    @PostMapping("/delete")
    public DeletedVO delete(@RequestBody List<Integer> ids) {
        testCaseNodeService.deleteNodes(ids);
        testCaseService.deleteCaseByNodeId(ids);
        return new DeletedVO();
    }

    @PostMapping("/drag")
    public UpdatedVO drag(@RequestBody TreeBO treeBO) {
        testCaseNodeService.nodeDrag(treeBO);
        return new UpdatedVO();
    }

    @PostMapping("/position")
    public UpdatedVO position(@RequestBody List<Integer> ids) {
        testCaseNodeService.nodePosition(ids);
        return new UpdatedVO();
    }

    @GetMapping("/{id}")
    public List<TreeBO> get(@PathVariable(value = "id") @Positive(message = "{id.positive}") Integer id) {

        return testCaseNodeService.selectNodeTreeByProjectId(id);
    }


}
