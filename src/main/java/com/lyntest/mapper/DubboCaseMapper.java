package com.lyntest.mapper;

import com.lyntest.bean.DubboCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/4
 */
@Repository
@Mapper
public interface DubboCaseMapper {

    /**
     * 查询case列表
     * @param dubboCase
     * @return
     */
    List<DubboCase> selectDubboCaseList(DubboCase dubboCase);

    /** 根据ids查询对应的caseList */
    List<DubboCase> selectDubboCaseListByIds(@Param("ids") Integer[] ids);

    /** 根据id查询对应的case */
    DubboCase selectDubboCase(Integer id);

    /** 获取创建人列表 */
    List<DubboCase> selectCreaterList();

    /** 创建一条新case */
    int createDubboCase(DubboCase dubboCase);

    /** 更新case */
    int updateDubboCase(DubboCase dubboCase);

    /** 删除case */
    int deleteDubboCase(@Param("ids") Integer[] ids);


}
