package com.lyntest.service;

import com.lyntest.bean.HttpCase;
import com.lyntest.bean.HttpCaseList;
import com.lyntest.bean.ResponseVo;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
public interface HttpCaseManageService {


    /** 获取创建人列表 */
    ResponseVo searchCreaterList();

    /**
     * @param httpCaseList
     * @return 获取case列表
     */
    ResponseVo searchHttpCaseList(HttpCaseList httpCaseList);

    /** 新增testCase */
    ResponseVo createHttpCase(HttpCase httpCase);

    /** 更新testCase */
    ResponseVo updateHttpCase(HttpCase httpCase);

    /** 删除 */
    ResponseVo deleteHttpCase(Integer[] ids);

    /** 执行请求 */
    ResponseVo excuteRequest(Integer[] ids);

}
