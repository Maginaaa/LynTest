package com.lyntest.service;

import com.lyntest.bean.*;


/**
 * @author 简单随风
 * @date 2019/10/25
 */
public interface CollectionManageService {

    /** 获取关注集合列表 */
    ResponseVo getFocusCollectionList();

    /** 获取所有集合列表 */
    ResponseVo getCollectionList(CollectionList collectionList);

    /** 获取创建人列表 */
    ResponseVo getCollectionCreaterList();

    /** 新增集合 */
    ResponseVo createNewCollection(CollectionDetail collectionDetail);

    /** 删除集合 */
    ResponseVo deleteCollection(int collectionId);

    /** 修改集合信息 */
    ResponseVo updateCollectionInfo(CollectionDetail collectionDetail);

    /** 获取集合详情 */
    ResponseVo getCollectionDetail(int collectionId);

    /** 集合新增变量 */
    ResponseVo insertCollectionVariable(Variable variable);

    /** 修改集合变量 */
    ResponseVo updateCollectionVariable(Variable variable);

    /** 删除集合变量 */
    ResponseVo deleteCollectionVariable(int id);

    /** 关注\取关集合 */
    ResponseVo collectionFocus(CollectionFocus collectionFocus);

    /** 查询集合内的接口 */
    ResponseVo getCollectionCaseList(Integer collectionId);

    /** 修改集合内的接口顺序 */
    ResponseVo updateCollectionCaseOrder(CollectionCaseList collectionCaseList);

    /** 删除集合中的某一条接口 */
    ResponseVo deleteCaseInCollection(CommonCase commonCase);

    /** 测试集合执行 */
    ResponseVo collectionExcute(Integer collectionId);

    /** 获取集合执行记录 */
    ResponseVo collectionExcuteRecords(CollectionList collectionList);

}
