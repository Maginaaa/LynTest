package com.lyn.service.autotest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.bo.TreeBO;
import com.lyn.dto.autotest.CollectionDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.model.autotest.CollectionDO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.model.autotest.PocketCaseDO;
import com.lyn.model.autotest.PocketDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-18
 */
public interface CollectionService extends IService<CollectionDO> {

    CollectionDTO getCollectionDetail(Integer id);

    /**
     * 获取定时任务开关处于开启状态的集合的id list
     * @return
     */
    List<CollectionDO> timingSwitchOpenCollectionIdList();

    /**
     * 新增集合
     * @return 新建集合的集合id
     */
    int createCollection(CollectionDTO collectionDTO);

    /**
     * 修改集合
     */
    boolean updateCollection(CollectionDTO collectionDTO);

    /**
     * 新增包
     */
    Integer createPocket(PocketDO pocketDO);

    /**
     * 修改包名
     */
    boolean updatePocket(PocketDO pocketDO);

    /**
     * 删除包
     */
    boolean deletePocket(Integer pocketId);

    /**
     * 获取集合内所有包和case，转成tree
     */
    List<TreeBO> getCaseTree(Integer collectionId);

    List<HttpCaseDO> getCaseList(Integer collectionId);

    /**
     * 包内新增case
     */
    boolean insertCaseToPocket(PocketCaseDO pocketCaseDO);

    /**
     * pocket内批量新增case
     */
    void batchInsertCaseToPocket(List<PocketCaseDO> list);

    /**
     * 包内删除case
     */
    boolean deleteCaseFromPocket(PocketCaseDO pocketCaseDO);

    /**
     * pocket内批量删除case
     */
    void batchDeleteCaseFromPocket(List<PocketCaseDO> list);


    /**
     * 搜索集合列表
     * @param searchDTO
     * @return
     */
    IPage<CollectionDO> searchCollectionList(SearchDTO searchDTO);

    PocketDO getPocket(Integer id);

    /**
     *
     * @param id
     * @return 根据id查询pocketCaseDO
     */
    PocketCaseDO getPocketCase(Integer id);

    /**
     * 节点拖拽
     * ids: [level, id,id,id,pocketId(level=2)]
     */
    void nodeDrag(List<Integer> ids);

}
