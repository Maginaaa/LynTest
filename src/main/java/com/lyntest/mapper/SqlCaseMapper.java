package com.lyntest.mapper;

import com.lyntest.bean.SqlCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/17
 */
@Repository
@Mapper
public interface SqlCaseMapper {

    /** 查询sql case列表 */
    List<SqlCase> selectSqlCaseList(SqlCase sqlCase);

    /** 查询一条sqlCase */
    SqlCase selectSqlCase(Integer id);

    /** 获取创建人列表 */
    List<SqlCase> selectCreaterList();

    /** 创建一条新case */
    int createSqlCase(SqlCase sqlCase);

    /** 更新case */
    int updateSqlCase(SqlCase sqlCase);

    /** 删除case */
    int deleteSqlCase(Integer id);

}
