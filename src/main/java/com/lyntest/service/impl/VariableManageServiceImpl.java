package com.lyntest.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyntest.bean.GlobalVariableList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.Variable;
import com.lyntest.mapper.ApiTestConfigMapper;
import com.lyntest.service.VariableManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/12
 */
@Service(value = "VariableManageService")
public class VariableManageServiceImpl implements VariableManageService {

    @Autowired
    private ApiTestConfigMapper apiTestConfigMapper;

    @Override
    public ResponseVo searchGlobalVariableList(GlobalVariableList globalVariableList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(globalVariableList.getPage(),globalVariableList.getPageSize());

        List<Variable> variableList = apiTestConfigMapper.selectGlobalVariable();

        PageInfo pageInfo = new PageInfo<>(variableList);

        if (variableList.size() > 0){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult(pageInfo);
        } else {
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("查询结果为空");
        }

        return responseVo;
    }

    @Override
    public ResponseVo createNewGlobalVariable(Variable variable) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.createGlobalVariable(variable);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo updateGlobalVariable(Variable variable) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.updateGlobalVariable(variable);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }
        return responseVo;
    }

    @Override
    public ResponseVo deleteGlobalVariable(int id) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.deleteGlobalVariable(id);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }
        return responseVo;
    }
}
