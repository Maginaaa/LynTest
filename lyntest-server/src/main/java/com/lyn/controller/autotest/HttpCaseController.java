package com.lyn.controller.autotest;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyn.bo.CreatorBO;
import com.lyn.bo.ExpectedBO;
import com.lyn.bo.HeadersBO;
import com.lyn.bo.VariableSaveBO;
import com.lyn.common.util.PageUtil;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.service.autotest.HttpCaseService;
import com.lyn.vo.*;
import io.github.talelin.core.annotation.LoginRequired;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
*  @author 简单随风
* @since 2020-09-04
*/
@RestController
@RequestMapping("/http")
public class HttpCaseController {

    @Autowired
    private HttpCaseService httpCaseService;

    @PostMapping("/create")
    @LoginRequired
    public CreatedVO create(@RequestBody @Validated HttpCaseDTO httpCaseDTO) {
        httpCaseService.saveHttpCase(httpCaseDTO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    @LoginRequired
    public UpdatedVO update(@RequestBody @Validated HttpCaseDTO httpCaseDTO) {
        httpCaseService.editHttpCase(httpCaseDTO);
        return new UpdatedVO();
    }

    @PostMapping("/delete")
    @LoginRequired
    public DeletedVO delete(@RequestBody Integer[] ids) {
        httpCaseService.deleteHttpCase(ids);
        return new DeletedVO();
    }


    @PostMapping("/list")
    public PageResponseVO<HttpCaseDTO> list(@RequestBody @Validated SearchDTO searchDTO) {

        IPage<HttpCaseDO> iPage = httpCaseService.searchHttpCaseList(searchDTO);
        List<HttpCaseDTO> newList = iPage.getRecords().stream().map(HttpCaseController::httpCaseDtoToDo).collect(Collectors.toList());
        return PageUtil.build(iPage, newList);
    }

    @PostMapping("/execute")
    public AutoTestResponseVO execute(@RequestBody @Validated HttpCaseDTO httpCaseDTO){

        httpCaseService.variableInit();
        return httpCaseService.casePreExecute(httpCaseDTO);
    }

    @GetMapping("/creator")
    public List<CreatorBO> creatorList() {
        return httpCaseService.getCreatorList();
    }

    /**
     * HttpCaseDO转HttpCaseDTO
     */
    private static HttpCaseDTO httpCaseDtoToDo(HttpCaseDO httpCaseDO){
        HttpCaseDTO httpCaseDTO = new HttpCaseDTO();
        BeanUtils.copyProperties(httpCaseDO, httpCaseDTO);
        httpCaseDTO.setHeaderForm(JSON.parseArray(httpCaseDO.getHeaderValue(), HeadersBO.class));
        httpCaseDTO.setVariableList(JSON.parseArray(httpCaseDO.getVariableList(), VariableSaveBO.class));
        httpCaseDTO.setExpectedList(JSON.parseArray(httpCaseDO.getExpectedList(), ExpectedBO.class));
        return httpCaseDTO;
    }

}
