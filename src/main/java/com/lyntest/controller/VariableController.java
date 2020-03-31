package com.lyntest.controller;

import com.lyntest.bean.GlobalVariableList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.Variable;
import com.lyntest.service.VariableManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 简单随风
 * @date 2019/10/24
 */
@Controller
@RequestMapping(value="/variable")
public class VariableController {

    @Autowired
    private VariableManageService variableManageService;

    /**
     * 获取全局变量列表
     */
    @ResponseBody
    @PostMapping(value = "/list")
    public ResponseVo searchVariableList(@RequestBody GlobalVariableList globalVariableList){

        return variableManageService.searchGlobalVariableList(globalVariableList);
    }

    /**
     * 新增全局变量
     */
    @ResponseBody
    @PostMapping(value = "/create")
    public ResponseVo createNewVariable(@RequestBody Variable variable){

        return variableManageService.createNewGlobalVariable(variable);
    }

    /**
     * 更新全局变量
     */
    @ResponseBody
    @PostMapping(value = "/update")
    public ResponseVo updateVariable(@RequestBody Variable variable){

        return variableManageService.updateGlobalVariable(variable);
    }

    /**
     * 删除全局变量
     */
    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseVo deleteVariable(@RequestBody Integer id){

        return variableManageService.deleteGlobalVariable(id);

    }


}
