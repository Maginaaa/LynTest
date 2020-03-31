package com.lyntest.controller;

import com.lyntest.bean.DubboCase;
import com.lyntest.bean.DubboCaseList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.service.DubboCaseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 简单随风
 * @date 2019/12/4
 */
@Controller
@RequestMapping(value="/dubbo")
public class DubboCaseController {

    @Autowired
    private DubboCaseManageService dubboCaseManageService;

    /**
     * 查询创建人列表
     */
    @ResponseBody
    @GetMapping(value = "/creater")
    public ResponseVo searchCreaterList(){

        return dubboCaseManageService.searchCreaterList();
    }

    /**
     * 查询case列表
     */
    @ResponseBody
    @PostMapping(value = "/search")
    public ResponseVo searchCaseList(@RequestBody DubboCaseList dubboCaseList){

        return dubboCaseManageService.searchDubboCaseList(dubboCaseList);
    }

    /**
     * 创建新case
     */
    @ResponseBody
    @PostMapping(value = "/create")
    public ResponseVo createNewCase(@RequestBody DubboCase dubboCase){

        return dubboCaseManageService.createDubboCase(dubboCase);
    }

    /**
     * 编辑case
     */
    @ResponseBody
    @PostMapping(value = "/update")
    public ResponseVo updateCase(@RequestBody DubboCase dubboCase){

        return dubboCaseManageService.updateDubboCase(dubboCase);

    }

    /**
     * 删除case
     */
    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseVo deleteCase(@RequestBody Integer[] ids){

        return dubboCaseManageService.deleteDubboCase(ids);

    }

    /**
     * 调试执行
     */
    @ResponseBody
    @PostMapping(value = "/excute")
    public ResponseVo excuteCase(@RequestBody Integer[] ids){

        return dubboCaseManageService.excuteRequest(ids);
    }
}
