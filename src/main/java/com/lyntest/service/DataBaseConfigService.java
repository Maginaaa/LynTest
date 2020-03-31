package com.lyntest.service;

import com.lyntest.bean.DataBase;
import com.lyntest.bean.DataBaseList;
import com.lyntest.bean.ResponseVo;

/**
 * @author 简单随风
 * @date 2019/12/16
 */
public interface DataBaseConfigService {

    ResponseVo getDataBaseList(DataBaseList dataBaseList);

    /** 新增变量 */
    ResponseVo createNewDataBase(DataBase dataBase);

    /** 修改变量 */
    ResponseVo updateDataBase(DataBase dataBase);

    /** 删除变量 */
    ResponseVo deleteDataBase(int id);
}
