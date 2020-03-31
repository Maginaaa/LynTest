package com.lyntest.controller;

import com.lyntest.bean.DataBase;
import com.lyntest.bean.DataBaseList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.service.DataBaseConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 简单随风
 * @date 2019/12/16
 */
@Slf4j
@Controller
@RequestMapping(value="/db")
public class DataBaseController {

    @Autowired
    private DataBaseConfigService dataBaseConfigService;

    /**
     * 获取数据库列表
     */
    @ResponseBody
    @PostMapping(value = "/list")
    public ResponseVo searchDataBaseList(@RequestBody DataBaseList dataBaseList){

        return dataBaseConfigService.getDataBaseList(dataBaseList);
    }

    /**
     * 新增数据库
     */
    @ResponseBody
    @PostMapping(value = "/create")
    public ResponseVo createNewDataBase(@RequestBody DataBase dataBase){

        return dataBaseConfigService.createNewDataBase(dataBase);
    }

    /**
     * 更新数据库
     */
    @ResponseBody
    @PostMapping(value = "/update")
    public ResponseVo updateDataBase(@RequestBody DataBase dataBase){

        return dataBaseConfigService.updateDataBase(dataBase);
    }

    /**
     * 删除数据库
     */
    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseVo deleteDataBase(@RequestBody Integer id){

        return dataBaseConfigService.deleteDataBase(id);

    }
}
