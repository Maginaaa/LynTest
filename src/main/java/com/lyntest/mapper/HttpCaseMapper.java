package com.lyntest.mapper;

import com.lyntest.bean.HttpCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/9/29
 */
@Repository
@Mapper
public interface HttpCaseMapper {

    /**
     * 查询case列表
     * @param httpCase 支持查询条件为 接口名称、系统(分类)、创建人
     * @return
     */
    List<HttpCase> selectHttpCaseList(HttpCase httpCase);

    /** 根据ids查询对应的caseList */
    List<HttpCase> selectHttpCaseListByIds(@Param("ids") Integer[] ids);

    /** 根据id查询对应case */
    HttpCase selectHttpCase(Integer id);

    /** 获取创建人列表 */
    List<HttpCase> selectCreaterList();

    /** 创建一条新case */
    int createHttpCase(HttpCase httpCase);

    /** 更新case */
    int updateHttpCase(HttpCase httpCase);

    /** 删除case */
    int deleteHttpCase(@Param("ids") Integer[] ids);



}
