package com.lyntest.controller;

import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.SqlCase;
import com.lyntest.bean.SqlCaseList;
import com.lyntest.service.SqlCaseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 简单随风
 * @date 2019/12/17
 */
@Controller
@RequestMapping(value="/sql")
public class SqlCaseController {

    @Autowired
    private SqlCaseManageService sqlCaseManageService;

    /**
     * 查询创建人列表
     */
    @ResponseBody
    @GetMapping(value = "/creater")
    public ResponseVo searchCreaterList(){

        return sqlCaseManageService.searchCreaterList();
    }

    /**
     * 查询case列表
     */
    @ResponseBody
    @PostMapping(value = "/search")
    public ResponseVo searchCaseList(@RequestBody SqlCaseList sqlCaseList){

        return sqlCaseManageService.searchSqlCaseList(sqlCaseList);
    }

    /**
     * 创建新case
     */
    @ResponseBody
    @PostMapping(value = "/create")
    public ResponseVo createNewCase(@RequestBody SqlCase sqlCase){

        return sqlCaseManageService.createSqlCase(sqlCase);
    }

    /**
     * 编辑case
     */
    @ResponseBody
    @PostMapping(value = "/update")
    public ResponseVo updateCase(@RequestBody SqlCase sqlCase){

        return sqlCaseManageService.updateSqlCase(sqlCase);

    }

    /**
     * 删除case
     */
    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseVo deleteCase(@RequestBody Integer id){

        return sqlCaseManageService.deleteSqlCase(id);

    }

    /**
     * 调试执行
     */
    @ResponseBody
    @PostMapping(value = "/excute")
    public ResponseVo excuteCase(@RequestBody SqlCase sqlCase){

        return sqlCaseManageService.excuteSqlRequest(sqlCase);
    }
}
