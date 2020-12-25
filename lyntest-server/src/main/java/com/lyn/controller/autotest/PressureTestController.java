package com.lyn.controller.autotest;

import com.alibaba.fastjson.JSON;
import com.lyn.async.PressureExecute;
import com.lyn.bo.ExpectedBO;
import com.lyn.bo.HeadersBO;
import com.lyn.bo.VariableSaveBO;
import com.lyn.common.LocalUser;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.dto.autotest.PressureReportDTO;
import com.lyn.model.UserDO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.service.autotest.HttpCaseService;
import com.lyn.service.autotest.PressureReportService;
import io.github.talelin.core.annotation.LoginRequired;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.lyn.async.ApiRequest.variableReplace;

/**
 * @author 简单随风
 * @date 2020/12/15
 */
@RestController
@RequestMapping("/pressure")
public class PressureTestController {

    @Autowired
    private HttpCaseService httpCaseService;

    @Autowired
    private PressureReportService pressureReportService;

    @Autowired
    private PressureExecute pressureExecute;

    @PostMapping("/execute")
    @LoginRequired
    public String execute(@RequestBody PressureReportDTO pressureReportDTO) {

        HttpCaseDO httpCaseDO = httpCaseService.getById(pressureReportDTO.getCaseId());
        HttpCaseDTO httpCaseDTO = httpCaseDtoToDo(httpCaseDO);

        httpCaseService.variableInit();

        // 变量替换
        variableReplace(httpCaseDTO);

        UserDO user = LocalUser.getLocalUser();
        pressureReportDTO.setExecuteName(user.getUsername());
        pressureReportDTO.setExecuteCode(user.getCode());
        pressureReportDTO.setExecuteTime(new Date());
        pressureReportDTO.setCaseId(httpCaseDTO.getId());
        String reportId = httpCaseDTO.getId() + "_" + System.currentTimeMillis();
        pressureReportDTO.setId(reportId);

        pressureExecute.execute(pressureReportDTO, httpCaseDTO);

        return reportId;

    }

    @GetMapping("/list/{caseId}")
    public List<String> getReportIdList(@PathVariable Integer caseId){
        return pressureReportService.getReportIdList(caseId);
    }

    @GetMapping("/{id}")
    public PressureReportDTO getReport(@PathVariable String id){
        return pressureReportService.getReportDetail(id);
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