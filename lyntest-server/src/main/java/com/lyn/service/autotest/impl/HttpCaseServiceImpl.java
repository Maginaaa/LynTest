package com.lyn.service.autotest.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.async.ApiRequest;
import com.lyn.async.AutoTestConfig;
import com.lyn.bo.*;
import com.lyn.common.LocalUser;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.mapper.autotest.GlobalVariableMapper;
import com.lyn.mapper.autotest.HttpCaseMapper;
import com.lyn.model.UserDO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.service.autotest.HttpCaseService;
import com.lyn.vo.AutoTestResponseVO;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lyn.async.ApiRequest.variableReplace;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-04
 */
@Service
@Transactional
public class HttpCaseServiceImpl extends ServiceImpl<HttpCaseMapper, HttpCaseDO> implements HttpCaseService {

    @Autowired
    private HttpCaseMapper httpCaseMapper;

    @Autowired
    private GlobalVariableMapper globalVariableMapper;

    @Override
    public IPage<HttpCaseDO> searchHttpCaseList(SearchDTO searchDTO) {

        // 查询出IPage<HttpCaseDO>
        Page<HttpCaseDO> pager = new Page<>(searchDTO.getPage(), searchDTO.getCount());
        LambdaQueryWrapper<HttpCaseDO> wrapper = new QueryWrapper<HttpCaseDO>().lambda();
        wrapper.like(!searchDTO.getCaseName().isEmpty(), HttpCaseDO::getCaseName, searchDTO.getCaseName())
                .eq(!searchDTO.getCategory().isEmpty(), HttpCaseDO::getCategory, searchDTO.getCategory())
                .eq(!searchDTO.getCreatorCode().isEmpty(), HttpCaseDO::getCreatorCode, searchDTO.getCreatorCode())
                .orderByDesc(HttpCaseDO::getId);

        // 转换为IPage<HttpCaseDTO>
        return httpCaseMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean saveHttpCase(HttpCaseDTO httpCaseDTO) {
        UserDO user = LocalUser.getLocalUser();
        HttpCaseDO httpCaseDO = caseDtoToDo(httpCaseDTO);
        httpCaseDO.setCreatorName(user.getUsername());
        httpCaseDO.setCreatorCode(user.getCode());
        httpCaseDO.setCreateDate(new Date());
        return httpCaseMapper.insert(httpCaseDO) > 0;
    }

    @Override
    public boolean editHttpCase(HttpCaseDTO httpCaseDTO) {
        UserDO user = LocalUser.getLocalUser();
        HttpCaseDO httpCaseDO = caseDtoToDo(httpCaseDTO);
        httpCaseDO.setUpdaterName(user.getUsername());
        httpCaseDO.setUpdaterCode(user.getCode());
        httpCaseDO.setUpdateDate(new Date());
        return httpCaseMapper.updateById(httpCaseDO) > 0;
    }

    @Override
    public boolean deleteHttpCase(Integer[] ids) {

        return httpCaseMapper.deleteBatchIds(Arrays.asList(ids)) > 0;
    }

    @Override
    public AutoTestResponseVO casePreExecute(HttpCaseDTO httpCaseDTO) {
        // 变量替换
        variableReplace(httpCaseDTO);
        // 接口执行
        return caseDoExecute(httpCaseDTO);

    }

    @Override
    public AutoTestResponseVO caseDoExecute(HttpCaseDTO httpCaseDTO) {

        // 原始数据准备
        AutoTestResponseVO autoTestResponseVO = new AutoTestResponseVO();
        // 断言
        List<ExpectedBO> expectedBOList = new ArrayList<>();
        // 保存变量
        List<VariableSaveBO> variableList = new ArrayList<>();

        autoTestResponseVO.setCaseId(httpCaseDTO.getId());
        autoTestResponseVO.setCaseName(httpCaseDTO.getCaseName());
        autoTestResponseVO.setUrl(httpCaseDTO.getApiUrl());
        autoTestResponseVO.setRequestText(httpCaseDTO.getBodyValue());

        ResponseBO responseBO = ApiRequest.doHttpRequest(httpCaseDTO);
        // 请求失败则直接抛错
        if (!responseBO.getSuccess()){
            autoTestResponseVO.setResponseText(responseBO.getMsg());
            autoTestResponseVO.setExpectedIsPass(Boolean.FALSE);
            autoTestResponseVO.setExpectedRes(expectedBOList);
            autoTestResponseVO.setVariableList(variableList);
            return autoTestResponseVO;
        }
        Response response = responseBO.getResponse();
        // 获取响应时间
        Long resTime = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
        // 获取响应代码
        Integer statusCode = response.code();
        // 获取message
        String message = response.message();
        // 响应内容处理
        String res = "";
        try {
            assert response.body() != null;
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 断言
        boolean allCaseIsPass = caseAssert(httpCaseDTO, res,  expectedBOList);

        // 变量保存
        variableSave(httpCaseDTO, res, variableList);

        autoTestResponseVO.setResponseText(res);
        autoTestResponseVO.setResponseTime(resTime);
        autoTestResponseVO.setStatusCode(statusCode);
        autoTestResponseVO.setExpectedIsPass(allCaseIsPass);
        autoTestResponseVO.setExpectedRes(expectedBOList);
        autoTestResponseVO.setVariableList(variableList);
        autoTestResponseVO.setMessage(message);

        return autoTestResponseVO;
    }

    @Override
    public List<CreatorBO> getCreatorList() {

        return httpCaseMapper.selectCreatorList();
    }

    @Override
    public void variableInit() {
        // 获取全局变量
        globalVariableMapper.selectList(null).forEach( e->{
            AutoTestConfig.variableMap.put(e.getVariableName(),e.getVariableValue());
        });
    }


    /**
     * HttpCaseDTO转HttpCaseDO
     */
    private HttpCaseDO caseDtoToDo(HttpCaseDTO httpCaseDTO){
        HttpCaseDO httpCaseDO = new HttpCaseDO();
        BeanUtils.copyProperties(httpCaseDTO, httpCaseDO);
        httpCaseDO.setHeaderValue(JSON.toJSONString(httpCaseDTO.getHeaderForm()));
        httpCaseDO.setVariableList(JSON.toJSONString(httpCaseDTO.getVariableList()));
        httpCaseDO.setExpectedList(JSON.toJSONString(httpCaseDTO.getExpectedList()));
        return httpCaseDO;
    }

    @Override
    public HttpCaseDTO caseDoToDto(HttpCaseDO httpCaseDO){
        HttpCaseDTO httpCaseDTO = new HttpCaseDTO();
        BeanUtils.copyProperties(httpCaseDO, httpCaseDTO);
        httpCaseDTO.setHeaderForm(JSON.parseArray(httpCaseDO.getHeaderValue(), HeadersBO.class));
        httpCaseDTO.setVariableList(JSON.parseArray(httpCaseDO.getVariableList(), VariableSaveBO.class));
        httpCaseDTO.setExpectedList(JSON.parseArray(httpCaseDO.getExpectedList(), ExpectedBO.class));
        return httpCaseDTO;
    }

    /**
     *
     * @param httpCaseDTO 测试case列表
     * @param res 响应报文
     * @param expectedBOList 保存断言结果
     * @return 是否通过所有断言
     */
    private static Boolean caseAssert(HttpCaseDTO httpCaseDTO,String res, List<ExpectedBO> expectedBOList){
        boolean allCaseIsPass = Boolean.TRUE;
        for (ExpectedBO expectedBO:httpCaseDTO.getExpectedList()){
            if (expectedBO.getExtractMethod() == null
                    || expectedBO.getExtractRule()== null
                    || expectedBO.getCompareType() == null){
                continue;
            }
            // 判断未开启
            if (!expectedBO.getEnable()){
                continue;
            }
            // 提取方式：1.jsonPath  2.正则表达式
            int extractMethod = expectedBO.getExtractMethod();
            // 提取规则
            String extractRule = expectedBO.getExtractRule();
            // 校验方式：1.equals 2.contains
            int compareType = expectedBO.getCompareType();
            // 预期值
            String expectedValue = expectedBO.getExpectedValue();
            // 实际获取结果
            String actualRes = "";
            if (extractMethod == 1){
                // 这里取到的值如果是bool时，强转会抛错，所以需要用object接一下，再转String
                try {
                    Object o = JSONPath.extract(res, extractRule);
                    actualRes = String.valueOf(o);
                } catch (Exception e){
                    actualRes = String.valueOf(e);
                }
            } else if (extractMethod == 2){
                Pattern p = Pattern.compile(extractRule);
                Matcher m = p.matcher(res);
                if (m.find()){
                    actualRes = m.group(0);
                }
            }
            expectedBO.setActualValue(actualRes);

            boolean isPass = Boolean.FALSE;
            if (compareType == 1){
                isPass = actualRes.equals(expectedValue);
                expectedBO.setExtractSuccess(isPass);
            } else if (compareType == 2){
                isPass = actualRes.contains(expectedValue);
                expectedBO.setExtractSuccess(isPass);
            } else if (compareType == 3){
                isPass = !actualRes.isEmpty() && !"null".equals(actualRes);
                expectedBO.setExtractSuccess(isPass);
            }
            expectedBOList.add(expectedBO);
            allCaseIsPass = allCaseIsPass && isPass;
        }
        return allCaseIsPass;
    }

    /**
     *
     * @param httpCaseDTO 测试case列表
     * @param res 响应报文
     */
    private static void variableSave(HttpCaseDTO httpCaseDTO, String res, List<VariableSaveBO> variableList) {
        for (VariableSaveBO variableSaveBO:httpCaseDTO.getVariableList()){
            if (variableSaveBO.getExtractMethod() == null
                    || variableSaveBO.getExtractRule()== null
                    || variableSaveBO.getVariableName() == null){
                continue;
            }
            if (!variableSaveBO.getEnable()){
                continue;
            }
            // 提取方式：1.jsonPath  2.正则表达式
            int extractMethod = variableSaveBO.getExtractMethod();
            // 提取规则
            String extractRule = variableSaveBO.getExtractRule();
            // 变量名
            String variableName = variableSaveBO.getVariableName();
            // 实际获取结果
            String actualRes = "";
            if (extractMethod == 1){
                // 这里取到的值如果是bool时，强转会抛错，所以需要用object接一下，再转String
                try {
                  Object o = JSONPath.extract(res, extractRule);
                  actualRes = o != null ? String.valueOf(o) : "变量获取异常";
                } catch (Exception e){
                  actualRes = String.valueOf(e);
                }
            } else if (extractMethod == 2){
                Pattern p = Pattern.compile(extractRule);
                Matcher m = p.matcher(res);
                if (m.find()){
                    actualRes = m.group(0);
                }
            }
            variableSaveBO.setActualRes(actualRes);
            variableList.add(variableSaveBO);
            AutoTestConfig.variableMap.put(variableName, actualRes);
        }
    }



}
