package com.lyntest.controller;


import com.lyntest.bean.HttpCase;
import com.lyntest.bean.HttpCaseList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.service.HttpCaseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 简单随风
 * @date 2019/9/29
 */
@RestController
@RequestMapping(value="/http")
public class HttpCaseController {


    @Autowired
    private HttpCaseManageService httpCaseManageService;


    /**
     * 查询创建人列表
     */
    @GetMapping(value = "/creater")
    public ResponseVo searchCreaterList(){

        return httpCaseManageService.searchCreaterList();
    }



    /**
     * 查询case列表
     */
    @PostMapping(value = "/search")
    public ResponseVo searchCaseList(@RequestBody HttpCaseList httpCaseList){

        return httpCaseManageService.searchHttpCaseList(httpCaseList);
    }

    /**
     * 创建新case
     */
    @PostMapping(value = "/create")
    public ResponseVo createNewCase(@RequestBody HttpCase httpCase){

        ResponseVo responseVo = new ResponseVo();

        if (httpCase.getApiUrl().isEmpty()){
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("域名不能为空！");
            return responseVo;
        }

        return httpCaseManageService.createHttpCase(httpCase);
    }

    /**
     * 编辑case
     */
    @PostMapping(value = "/update")
    public ResponseVo updateCase(@RequestBody HttpCase httpCase){

        return httpCaseManageService.updateHttpCase(httpCase);

    }

    /**
     * 删除case
     */
    @PostMapping(value = "/delete")
    public ResponseVo deleteCase(@RequestBody Integer[] ids){

        return httpCaseManageService.deleteHttpCase(ids);

    }

    /**
     * 调试执行
     */
    @PostMapping(value = "/excute")
    public ResponseVo excuteCase(@RequestBody Integer[] ids){

        return httpCaseManageService.excuteRequest(ids);
    }

}
