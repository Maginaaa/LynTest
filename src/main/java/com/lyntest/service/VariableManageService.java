package com.lyntest.service;

import com.lyntest.bean.GlobalVariableList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.Variable;

/**
 * @author 简单随风
 * @date 2019/10/12
 */
public interface VariableManageService {

    /** 获取全局变量列表 */
    ResponseVo searchGlobalVariableList(GlobalVariableList globalVariableList);

    /** 新增变量 */
    ResponseVo createNewGlobalVariable(Variable variable);

    /** 修改变量 */
    ResponseVo updateGlobalVariable(Variable variable);

    /** 删除变量 */
    ResponseVo deleteGlobalVariable(int id);

}
