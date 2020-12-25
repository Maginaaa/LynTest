package com.lyn.controller.autotest;


import com.lyn.common.util.PageUtil;
import com.lyn.dto.PageBaseDTO;
import com.lyn.model.autotest.GlobalVariableDO;
import com.lyn.service.autotest.GlobalVariableService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

/**
*  @author 简单随风
* @since 2020-09-16
*/
@RestController
@RequestMapping("/variable")
public class GlobalVariableController {

    @Autowired
    private GlobalVariableService globalVariableService;

    @PostMapping("/create")
    public CreatedVO create(@RequestBody GlobalVariableDO globalVariableDO) {
        globalVariableService.createGlobalVariable(globalVariableDO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody GlobalVariableDO globalVariableDO) {
        globalVariableService.editGlobalVariable(globalVariableDO);
        return new UpdatedVO();
    }

    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        globalVariableService.deleteGlobalVariable(id);
        return new DeletedVO();
    }


    @PostMapping("/list")
    public PageResponseVO<GlobalVariableDO> page(@RequestBody @Validated PageBaseDTO pageBaseDTO) {
        return PageUtil.build(globalVariableService.searchVariableList(pageBaseDTO));
    }

}
