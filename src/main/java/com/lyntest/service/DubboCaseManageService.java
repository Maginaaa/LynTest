package com.lyntest.service;

import com.lyntest.bean.DubboCase;
import com.lyntest.bean.DubboCaseList;
import com.lyntest.bean.ResponseVo;

/**
 * @author 简单随风
 * @date 2019/12/4
 */
public interface DubboCaseManageService {

    /** 获取创建人列表 */
    ResponseVo searchCreaterList();

    /**
     * @param dubboCaseList
     * @return 获取case列表
     */
    ResponseVo searchDubboCaseList(DubboCaseList dubboCaseList);

    /** 新增testCase */
    ResponseVo createDubboCase(DubboCase dubboCase);

    /** 更新testCase */
    ResponseVo updateDubboCase(DubboCase dubboCase);

    /** 删除 */
    ResponseVo deleteDubboCase(Integer[] ids);

    /** 执行请求 */
    ResponseVo excuteRequest(Integer[] ids);
}
