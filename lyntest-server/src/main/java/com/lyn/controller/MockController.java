package com.lyn.controller;


import com.lyn.common.util.PageUtil;
import com.lyn.dto.mock.MockSearchDTO;
import com.lyn.model.MockDO;
import com.lyn.service.mock.MockService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;

/**
*  @author 简单随风
* @since 2020-09-17
*/
@RestController
@RequestMapping("/mock")
public class MockController {

    @Autowired
    private MockService mockService;

    @PostMapping("/create")
    public CreatedVO create(@RequestBody MockDO mockDO) {
        mockService.createMock(mockDO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody MockDO mockDO) {
        mockService.editMock(mockDO);
        return new UpdatedVO();
    }

    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        mockService.deleteMock(id);
        return new DeletedVO();
    }

    @PostMapping("/list")
    public PageResponseVO<MockDO> list(@RequestBody MockSearchDTO mockSearchDTO) {

        return PageUtil.build(mockService.searchMockList(mockSearchDTO));
    }

    @RequestMapping("/dynamic/**")
    public void mockTest(HttpServletRequest request, HttpServletResponse response){
        mockService.dynamicMock(request, response);
    }

}
