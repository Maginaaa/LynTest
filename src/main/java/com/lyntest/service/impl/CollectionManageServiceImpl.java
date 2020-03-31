package com.lyntest.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyntest.bean.*;
import com.lyntest.filter.HttpBasicAuthorizeAttribute;
import com.lyntest.mapper.*;
import com.lyntest.service.CollectionManageService;
import com.lyntest.utils.ApiTestConfig;
import com.lyntest.utils.CollectionDetailExcute;
import com.lyntest.utils.ExtentTestNGIReporterListener;
import com.lyntest.utils.MultiThreadingListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.TestNG;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/25
 */
@Service(value = "CollectionManageService")
public class CollectionManageServiceImpl implements CollectionManageService {


    @Autowired
    private ApiTestCollectionMapper apiTestCollectionMapper;

    @Autowired
    private HttpCaseMapper httpCaseMapper;

    @Autowired
    private DubboCaseMapper dubboCaseMapper;

    @Autowired
    private SqlCaseMapper sqlCaseMapper;

    @Autowired
    private ApiTestConfigMapper apiTestConfigMapper;

    @Autowired
    private CollectionCaseManageMapper collectionCaseManageMapper;

    @Override
    public ResponseVo getFocusCollectionList() {

        ResponseVo responseVo = new ResponseVo();

        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        List<CollectionDetail> list = apiTestCollectionMapper.selectFocusCollectionList(u.getCode());

        responseVo.setResult(list);

        return responseVo;
    }

    @Override
    public ResponseVo getCollectionList(CollectionList collectionList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(collectionList.getPage(), collectionList.getPageSize());

        List<CollectionDetail> list = apiTestCollectionMapper.selectCollectionList(collectionList.getCollectionDetail());

        PageInfo pageInfo = new PageInfo<>(list);

        responseVo.setResult(pageInfo);

        return responseVo;
    }

    @Override
    public ResponseVo getCollectionCreaterList() {

        ResponseVo responseVo = new ResponseVo();
        List<CollectionDetail> list = apiTestCollectionMapper.selectCollectionCreaterList();

        responseVo.setResult(list);
        return responseVo;
    }

    @Override
    public ResponseVo createNewCollection(CollectionDetail collectionDetail) {

        ResponseVo responseVo = new ResponseVo();

        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        collectionDetail.setCreaterName(u.getName());
        collectionDetail.setCreaterCode(u.getCode());

        int i = apiTestCollectionMapper.insertNewCollection(collectionDetail);

        // 创建时默认关注
        CollectionFocus collectionFocus = new CollectionFocus();
        collectionFocus.setUserCode(u.getCode());
        collectionFocus.setCollectionId(collectionDetail.getId());
        collectionFocus.setFocusDate(new Date());
        apiTestCollectionMapper.insertCollectionFocus(collectionFocus);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }
        return responseVo;

    }

    @Override
    public ResponseVo deleteCollection(int collectionId) {

        ResponseVo responseVo = new ResponseVo();

        // 先删除集合-api关联表内的所有api
        collectionCaseManageMapper.deleteAllCase(collectionId);

        // 再删除集合
        int i = apiTestCollectionMapper.deleteCollection(collectionId);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }
        return responseVo;
    }

    @Override
    public ResponseVo updateCollectionInfo(CollectionDetail collectionDetail) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestCollectionMapper.updateCollection(collectionDetail);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("修改成功！");
        }
        return responseVo;

    }

    @Override
    public ResponseVo getCollectionDetail(int collectionId) {

        ResponseVo responseVo = new ResponseVo();

        // 查询集合主要信息
        CollectionDetail collectionDetail =  apiTestCollectionMapper.selectCollectionDetail(collectionId);

        // 查询是否关注了集合
        CollectionFocus t = new CollectionFocus();
        t.setCollectionId(collectionId);
        t.setUserCode(HttpBasicAuthorizeAttribute.getTokenUserInfo().get().getCode());
        if (apiTestCollectionMapper.selectCollectionFocusType(t)){
            collectionDetail.setIsFocus(Boolean.TRUE);
        } else {
            collectionDetail.setIsFocus(Boolean.FALSE);
        }

        // 查询集合内的变量
        collectionDetail.setVariableList(apiTestCollectionMapper.selectCollectionVariable(collectionId));

        responseVo.setIsSuccess(Boolean.TRUE);
        responseVo.setResult(collectionDetail);

        return responseVo;
    }

    @Override
    public ResponseVo insertCollectionVariable(Variable variable) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestCollectionMapper.insertCollectionVariable(variable);
        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        } else {
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setResult("创建失败！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo updateCollectionVariable(Variable variable) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestCollectionMapper.updateCollectionVariable(variable);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }
        return responseVo;
    }

    @Override
    public ResponseVo deleteCollectionVariable(int collectionId) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestCollectionMapper.deleteCollectionVariable(collectionId);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }
        return responseVo;
    }

    @Override
    public ResponseVo collectionFocus(CollectionFocus collectionFocus) {

        ResponseVo responseVo = new ResponseVo();


        collectionFocus.setUserCode(HttpBasicAuthorizeAttribute.getTokenUserInfo().get().getCode());

        Boolean focusType = collectionFocus.getFocusType();
        if (focusType){
            // 关注
            if (apiTestCollectionMapper.selectCollectionFocusType(collectionFocus)){
                responseVo.setIsSuccess(Boolean.FALSE);
                responseVo.setMsg("已关注，请勿重复关注");
            } else {
                collectionFocus.setFocusDate(new Date());
                apiTestCollectionMapper.insertCollectionFocus(collectionFocus);
                responseVo.setIsSuccess(Boolean.TRUE);
                responseVo.setResult("关注成功");
            }
        } else {
            // 取关
            if (!apiTestCollectionMapper.selectCollectionFocusType(collectionFocus)){
                responseVo.setIsSuccess(Boolean.FALSE);
                responseVo.setMsg("并未关注集合，取消关注失败");
            } else {
                apiTestCollectionMapper.deleteCollectionFocus(collectionFocus);
                responseVo.setIsSuccess(Boolean.TRUE);
                responseVo.setResult("取关成功");
            }
        }
        return responseVo;
    }

    @Override
    public ResponseVo getCollectionCaseList(Integer collectionId) {

        ResponseVo responseVo = new ResponseVo();

        List<CommonCase> caseList = collectionCaseManageMapper.selectCommonCase(collectionId);

        responseVo.setResult(caseList);
        return responseVo;
    }

    @Override
    public ResponseVo updateCollectionCaseOrder(CollectionCaseList collectionCaseList) {

        ResponseVo responseVo = new ResponseVo();

        // 先删除所有api
        collectionCaseManageMapper.deleteAllCase(collectionCaseList.getCollectionId());

        // 再新增api
        collectionCaseManageMapper.insertCaseToCollection(collectionCaseList);

        responseVo.setIsSuccess(Boolean.TRUE);

        return responseVo;
    }

    @Override
    public ResponseVo deleteCaseInCollection(CommonCase commonCase) {

        ResponseVo responseVo = new ResponseVo();

        int i = collectionCaseManageMapper.deleteOneCase(commonCase);
        if (i != 1){
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("删除数据异常");
            return responseVo;
        }

        responseVo.setIsSuccess(Boolean.TRUE);
        responseVo.setResult("删除成功");
        return responseVo;
    }


    @Override
    public ResponseVo collectionExcute(Integer collectionId) {

        ResponseVo responseVo = new ResponseVo();

        // 获取完整的case列表
        ApiTestConfig.caseList = collectionCaseManageMapper.selectCommonCase(collectionId);

        // 遍历caseList，给caseInfo进行赋值，获取完整的case信息
        for (CommonCase commonCase:ApiTestConfig.caseList){
            Integer caseType = commonCase.getCaseType();
            switch (caseType){
                case ApiTestConfig.HTTP_CASE_TYPE:
                    commonCase.setCaseInfo(httpCaseMapper.selectHttpCase(commonCase.getCaseId()));
                    break;
                case ApiTestConfig.DUBBO_CASE_TYPE:
                    commonCase.setCaseInfo(dubboCaseMapper.selectDubboCase(commonCase.getCaseId()));
                    break;
                case ApiTestConfig.SQL_CASE_TYPE:
                    SqlCase sqlCase = sqlCaseMapper.selectSqlCase(commonCase.getCaseId());
                    DataBase dataBase = apiTestConfigMapper.selectDataBaseById(sqlCase.getDatabaseId());
                    dataBase.setSqlCase(sqlCase);
                    commonCase.setCaseInfo(dataBase);
                    break;
                default:
                    break;
            }
        }

        ApiTestConfig.collectionId = collectionId;

        // 全局变量赋值
        List<Variable> globalVariableList = apiTestConfigMapper.selectGlobalVariable();
        for (Variable variable:globalVariableList){
            ApiTestConfig.globalVariableMap.put(variable.getVariableName(),variable.getVariableValue());
        }

        // 集合变量赋值
        List<Variable> collectionVariableList = apiTestCollectionMapper.selectCollectionVariable(collectionId);
        for (Variable variable:collectionVariableList){
            ApiTestConfig.collectionVariableMap.put(variable.getVariableName(),variable.getVariableValue());
        }

        // 测试报告保存路径
        ApiTestConfig.reportName = String.valueOf(System.currentTimeMillis())+ ".html";
        CollectionDetail collectionDetail =  apiTestCollectionMapper.selectCollectionDetail(collectionId);
        collectionDetail.setId(collectionId);
        collectionDetail.setReportPath("/report/" + ApiTestConfig.reportName);

        // 更新最后执行人和最后执行时间
        User u = HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
        collectionDetail.setExcuterCode(u.getCode());
        collectionDetail.setExcuterName(u.getName());
        collectionDetail.setExcuteDatetime(new Date());
        apiTestCollectionMapper.updateCollection(collectionDetail);

        // 新增执行记录
        apiTestCollectionMapper.insertExcuteRecords(collectionDetail);

        // 测试报告推送至企业微信
//        if (collectionDetail.getWxPush()){
//            String content = "接口测试平台报告 \n";
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            content += "【测试时间】" + sdf.format(new Date()) + "\n";
//            content += "【测试报告】http://10.230.27.158:8080/report/" + ApiTestConfig.reportName;
//            WxMsgPush.wxMsgPush(u.getCode(), content);
//        }


        // 并发次数
        ApiTestConfig.repeatTimes = collectionDetail.getRepeatTimes();
        ApiTestConfig.threadPoolSize = collectionDetail.getThreadPoolSize();

        // 测试执行
        TestNG testNg = new TestNG();
        Class[] listenerClass = {ExtentTestNGIReporterListener.class, MultiThreadingListener.class};
        testNg.setListenerClasses(Arrays.asList(listenerClass));

        testNg.setTestClasses(new Class[]{CollectionDetailExcute.class});
        testNg.run();

        responseVo.setIsSuccess(Boolean.TRUE);
        responseVo.setResult("执行完毕");

        return responseVo;
    }

    @Override
    public ResponseVo collectionExcuteRecords(CollectionList collectionList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(collectionList.getPage(), collectionList.getPageSize());

        List<CollectionDetail> list = apiTestCollectionMapper.selectExcuteRecords(collectionList.getCollectionDetail());

        if (list.size()>0){
            PageInfo pageInfo = new PageInfo<>(list);
            responseVo.setResult(pageInfo);
            responseVo.setIsSuccess(Boolean.TRUE);
        }

        return responseVo;
    }
}
