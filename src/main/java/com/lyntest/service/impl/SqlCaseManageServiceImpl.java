package com.lyntest.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyntest.bean.*;
import com.lyntest.filter.HttpBasicAuthorizeAttribute;
import com.lyntest.mapper.ApiTestConfigMapper;
import com.lyntest.mapper.SqlCaseMapper;
import com.lyntest.service.SqlCaseManageService;
import com.lyntest.utils.ApiTestConfig;
import com.lyntest.utils.ApiTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/17
 */
@Service(value = "SqlCaseManageService")
public class SqlCaseManageServiceImpl implements SqlCaseManageService {

    @Autowired
    private SqlCaseMapper sqlCaseMapper;

    @Autowired

    private ApiTestConfigMapper apiTestConfigMapper;

    @Override
    public ResponseVo searchCreaterList() {

        ResponseVo responseVo = new ResponseVo();
        List<SqlCase> list = sqlCaseMapper.selectCreaterList();
        if (list.size() != 0){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult(list);
        } else {
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("查询结果为空");
        }
        return responseVo;
    }

    @Override
    public ResponseVo searchSqlCaseList(SqlCaseList sqlCaseList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(sqlCaseList.getPage(), sqlCaseList.getPageSize());

        List<SqlCase> list = sqlCaseMapper.selectSqlCaseList(sqlCaseList.getSqlCase());

        // 遍历list，给header，formValue,variableList,expectedList赋值
        for (SqlCase sqlCase :list){
            sqlCase.setVariableList(JSON.parseArray(sqlCase.getVariableListValue(), VariableSave.class));
        }

        PageInfo pageInfo = new PageInfo<>(list);

        responseVo.setResult(pageInfo);

        return responseVo;
    }

    @Override
    public ResponseVo createSqlCase(SqlCase sqlCase) {

        ResponseVo responseVo = new ResponseVo();

        // 添加创建人信息
        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        sqlCase.setCreaterCode(u.getCode());
        sqlCase.setCreaterName(u.getName());
        sqlCase.setCreateDate(new Date());

        sqlCase.setVariableListValue(JSON.toJSONString(sqlCase.getVariableList()));

        int i = sqlCaseMapper.createSqlCase(sqlCase);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo updateSqlCase(SqlCase sqlCase) {


        ResponseVo responseVo = new ResponseVo();

        // 给variableListValue赋值
        sqlCase.setVariableListValue(JSON.toJSONString(sqlCase.getVariableList()));

        int i = sqlCaseMapper.updateSqlCase(sqlCase);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo deleteSqlCase(Integer id) {

        ResponseVo responseVo = new ResponseVo();

        int i = sqlCaseMapper.deleteSqlCase(id);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo excuteSqlRequest(SqlCase sqlCase) {

        ResponseVo responseVo = new ResponseVo();

        DataBase dataBase = apiTestConfigMapper.selectDataBaseById(sqlCase.getDatabaseId());
        dataBase.setSqlCase(sqlCase);

        String result = ApiTestUtils.doSqlRequest(dataBase);
        sqlCase.setResult(result);

        // 保存变量
        ApiTestUtils.saveVariable(result, sqlCase.getVariableListValue(), ApiTestConfig.GLOBAL_COLLECTION_ID);

        // 更新数据库保存的信息
        sqlCaseMapper.updateSqlCase(sqlCase);

        // 将执行后的结果返回给前端
        responseVo.setIsSuccess(Boolean.TRUE);
        responseVo.setResult(sqlCase);

        return responseVo;
    }


}
