package com.lyntest.service;

import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.SqlCase;
import com.lyntest.bean.SqlCaseList;

/**
 * @author 简单随风
 * @date 2019/12/17
 */
public interface SqlCaseManageService {

    /** 获取创建人列表 */
    ResponseVo searchCreaterList();

    /**
     * @return 获取case列表
     */
    ResponseVo searchSqlCaseList(SqlCaseList sqlCaseList);

    /** 新增testCase */
    ResponseVo createSqlCase(SqlCase sqlCase);

    /** 更新testCase */
    ResponseVo updateSqlCase(SqlCase sqlCase);

    /** 删除 */
    ResponseVo deleteSqlCase(Integer id);

    /** 执行请求 */
    ResponseVo excuteSqlRequest(SqlCase sqlCase);
}
