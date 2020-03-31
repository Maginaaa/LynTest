package com.lyntest.mapper;

import com.lyntest.bean.CollectionDetail;
import com.lyntest.bean.CollectionFocus;
import com.lyntest.bean.Variable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/24
 */
@Repository
@Mapper
public interface ApiTestCollectionMapper {

    /** 查询用户所关注的所有集合 */
    List<CollectionDetail>  selectFocusCollectionList(String userCode);

    /** 查询集合列表 */
    List<CollectionDetail>  selectCollectionList(CollectionDetail collectionDetail);

    /** 获取创建人列表 */
    List<CollectionDetail> selectCollectionCreaterList();

    /** 新增集合 */
    int insertNewCollection(CollectionDetail collectionDetail);

    /** 删除集合 */
    int deleteCollection(int collectionId);

    /** 查询集合详情 */
    CollectionDetail selectCollectionDetail(int collectionId);

    /** 查询集合的关注状态 */
    Boolean selectCollectionFocusType(CollectionFocus collectionFocus);

    /** 关注集合 */
    int insertCollectionFocus(CollectionFocus collectionFocus);

    /** 取消关注集合 */
    int deleteCollectionFocus(CollectionFocus collectionFocus);

    /** 查询集合内的变量列表 */
    List<Variable> selectCollectionVariable(int collectionId);

    /** 集合新增变量 */
    int insertCollectionVariable(Variable variable);

    /** 修改集合变量 */
    int updateCollectionVariable(Variable variable);

    /** 删除集合变量 */
    int deleteCollectionVariable(int id);

    /** 通过集合id,查询所属分类 */
    String[] selectCollectionCategory(int collectionId);

    /** 修改测试集合 */
    int updateCollection(CollectionDetail collectionDetail);

    /** 执行记录 */
    int insertExcuteRecords(CollectionDetail collectionDetail);

    /** 查询集合执行记录 */
    List<CollectionDetail> selectExcuteRecords(CollectionDetail collectionDetail);

}
