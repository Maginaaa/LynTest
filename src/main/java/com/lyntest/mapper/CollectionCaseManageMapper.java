package com.lyntest.mapper;

import com.lyntest.bean.CollectionCaseList;
import com.lyntest.bean.CommonCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/24
 */
@Repository
@Mapper
public interface CollectionCaseManageMapper {

    /**
     * 获取集合内的所有CommonCase
     * @param collectionId
     * @return
     */
    List<CommonCase> selectCommonCase(Integer collectionId);


    /** 修改集合内api的顺序 */
    int deleteAllCase(int collectionId);
    int insertCaseToCollection(CollectionCaseList collectionCaseList);

    /** 删除集合内的单条case */
    int deleteOneCase(CommonCase commonCase);

}
