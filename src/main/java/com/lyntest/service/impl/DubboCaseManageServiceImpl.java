package com.lyntest.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyntest.bean.*;
import com.lyntest.filter.HttpBasicAuthorizeAttribute;
import com.lyntest.mapper.ApiTestConfigMapper;
import com.lyntest.mapper.DubboCaseMapper;
import com.lyntest.service.DubboCaseManageService;
import com.lyntest.utils.ApiTestConfig;
import com.lyntest.utils.ApiTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/4
 */
@Slf4j
@Service(value = "DubboCaseManageService")
public class DubboCaseManageServiceImpl implements DubboCaseManageService {

    @Autowired
    private DubboCaseMapper dubboCaseMapper;

    @Autowired
    private ApiTestConfigMapper apiTestConfigMapper;


    @Override
    public ResponseVo searchCreaterList() {

        ResponseVo responseVo = new ResponseVo();
        List<DubboCase> list = dubboCaseMapper.selectCreaterList();

        responseVo.setResult(list);
        return responseVo;
    }

    @Override
    public ResponseVo searchDubboCaseList(DubboCaseList dubboCaseList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(dubboCaseList.getPage(), dubboCaseList.getPageSize());

        List<DubboCase> list = dubboCaseMapper.selectDubboCaseList(dubboCaseList.getDubboCase());

        // 遍历list，给variableList,expectedList赋值
        for (DubboCase dubboCase :list){
            setJsonValue(dubboCase);

        }

        PageInfo pageInfo = new PageInfo<>(list);

        responseVo.setResult(pageInfo);

        return responseVo;
    }

    @Override
    public ResponseVo createDubboCase(DubboCase dubboCase) {

        ResponseVo responseVo = new ResponseVo();

        // 添加创建人信息
        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        dubboCase.setCreaterCode(u.getCode());
        dubboCase.setCreaterName(u.getName());
        dubboCase.setCreateDate(new Date());

        // 设置接口nameValue
        dubboCase.setApiName("dubbo_" + System.currentTimeMillis());

        // 最后更新人的信息清空(copy创建的时候可能会带上该更新人信息)
        dubboCase.setUpdateDate(null);
        dubboCase.setUpdaterCode(null);
        dubboCase.setUpdaterName(null);

        // 给variableListValue,expectedListValue赋值
        setValueForJson(dubboCase);

        // 执行状态置为失败
        dubboCase.setStatus(Boolean.FALSE);

        int i = dubboCaseMapper.createDubboCase(dubboCase);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo updateDubboCase(DubboCase dubboCase){

        ResponseVo responseVo = new ResponseVo();

        // 添加修改人信息
        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        dubboCase.setUpdaterCode(u.getCode());
        dubboCase.setUpdaterName(u.getName());
        dubboCase.setUpdateDate(new Date());

        // 给headerValue，formValue,variableListValue,expectedListValue赋值
        setValueForJson(dubboCase);

        int i = dubboCaseMapper.updateDubboCase(dubboCase);

        if (i==1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo deleteDubboCase(Integer[] ids) {

        ResponseVo responseVo = new ResponseVo();

        int i = dubboCaseMapper.deleteDubboCase(ids);

        if (i==ids.length){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo excuteRequest(Integer[] ids) {

        ResponseVo responseVo = new ResponseVo();

        // 全局变量赋值
        List<Variable> variableList = apiTestConfigMapper.selectGlobalVariable();
        for (Variable variable:variableList){
            ApiTestConfig.globalVariableMap.put(variable.getVariableName(), variable.getVariableValue());
        }

        // 获取当前选中的所有case
        List<DubboCase> caseList = dubboCaseMapper.selectDubboCaseListByIds(ids);
        // 遍历caseList，进行http请求
        for (DubboCase dubboCase :caseList){

            String result = ApiTestUtils.doDubboRequest(dubboCase, ApiTestConfig.GLOBAL_COLLECTION_ID);

            // 保存变量
            ApiTestUtils.saveVariable(result, dubboCase.getVariableListValue(), ApiTestConfig.GLOBAL_COLLECTION_ID);

            // 判断是否通过了所有校验条件
            if (ApiTestUtils.verifyResult(result, dubboCase.getExpectedListValue(), ApiTestConfig.GLOBAL_COLLECTION_ID)){
                dubboCase.setStatus(Boolean.TRUE);
            } else {
                dubboCase.setStatus(Boolean.FALSE);
            }
            // 给testCase的执行状态进行赋值
            dubboCase.setResult(result);

            // 更新数据库保存的信息
            setJsonValue(dubboCase);
            dubboCaseMapper.updateDubboCase(dubboCase);

            // 将执行后的结果返回给前端
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult(dubboCase);

        }
        return responseVo;
    }

    /**
     * 给variableList,expectedList赋值
     */
    private void setJsonValue(DubboCase dubboCase){
        dubboCase.setVariableList(JSON.parseArray(dubboCase.getVariableListValue(), VariableSave.class));
        dubboCase.setExpectedList(JSON.parseArray(dubboCase.getExpectedListValue(), Expected.class));

    }

    /**
     * 给variableListValue,expectedListValue赋值
     */
    private void setValueForJson(DubboCase dubboCase){
        dubboCase.setVariableListValue(JSON.toJSONString(dubboCase.getVariableList()));
        dubboCase.setExpectedListValue(JSON.toJSONString(dubboCase.getExpectedList()));
    }
}
