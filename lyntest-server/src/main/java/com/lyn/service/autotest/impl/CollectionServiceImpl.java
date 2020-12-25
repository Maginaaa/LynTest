package com.lyn.service.autotest.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.bo.TreeBO;
import com.lyn.dto.autotest.CollectionDTO;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.mapper.autotest.CollectionMapper;
import com.lyn.mapper.autotest.PocketCaseMapper;
import com.lyn.mapper.autotest.PocketMapper;
import com.lyn.model.autotest.CollectionDO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.model.autotest.PocketCaseDO;
import com.lyn.model.autotest.PocketDO;
import com.lyn.service.autotest.CollectionService;
import com.lyn.service.autotest.HttpCaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-18
 */
@Service
@Transactional
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, CollectionDO> implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private PocketMapper pocketMapper;

    @Autowired
    private PocketCaseMapper pocketCaseMapper;

    @Autowired
    private HttpCaseService httpCaseService;

    @Override
    public CollectionDTO getCollectionDetail(Integer id) {

        CollectionDO collectionDO = collectionMapper.selectById(id);
        CollectionDTO collectionDTO = new CollectionDTO();
        BeanUtils.copyProperties(collectionDO, collectionDTO);
        if (collectionDO.getPushList() != null && !collectionDO.getPushList().isEmpty()){
            collectionDTO.setPushList(Arrays.stream(collectionDO.getPushList().split(",")).collect(Collectors.toList()));
        }

        return collectionDTO;
    }

    @Override
    public List<CollectionDO> timingSwitchOpenCollectionIdList() {
        return new LambdaQueryChainWrapper<>(collectionMapper)
                .select(CollectionDO::getId, CollectionDO::getCron)
                .eq(CollectionDO::getTimingSwitch, Boolean.TRUE)
                .list();
    }

    @Override
    public int createCollection(CollectionDTO collectionDTO) {
        // 新增集合
        CollectionDO collectionDO = collectionDtoToDo(collectionDTO);
        collectionMapper.insert(collectionDO);
        int collectionId = collectionDO.getId();
        // 集合内新增默认包
        PocketDO pocketDO = new PocketDO();
        pocketDO.setCollectionId(collectionId);
        pocketDO.setPocketName("默认接口");
        pocketDO.setPos(1024);
        pocketMapper.insert(pocketDO);
        return collectionId;
    }

    @Override
    public boolean updateCollection(CollectionDTO collectionDTO) {

        return collectionMapper.updateById(collectionDtoToDo(collectionDTO)) > 0;
    }

    @Override

    public Integer createPocket(PocketDO pocketDO) {
        Integer pos = getPocketPos(pocketDO.getCollectionId());
        pocketDO.setPos(pos);
        pocketMapper.insert(pocketDO);
        return  pocketDO.getId();
    }

    @Override
    public boolean updatePocket(PocketDO pocketDO) {
        return pocketMapper.updateById(pocketDO) > 0;
    }

    @Override
    public boolean deletePocket(Integer pocketId) {
        // 先删除pocket内的所有case
        LambdaQueryWrapper<PocketCaseDO> wrapper = new QueryWrapper<PocketCaseDO>().lambda();
        wrapper.eq(PocketCaseDO::getPocketId, pocketId);
        pocketCaseMapper.delete(wrapper);
        // 删除pocket
        return pocketMapper.deleteById(pocketId) > 0;
    }

    @Override
    public List<TreeBO> getCaseTree(Integer collectionId) {

        // 查询所有pocket
        List<PocketDO> pocketList = new LambdaQueryChainWrapper<>(pocketMapper).eq(PocketDO::getCollectionId, collectionId)
                .orderByAsc(PocketDO::getPos)
                .list();

        List<TreeBO> pocketTreeList = new ArrayList<>(pocketList.size());

        for (PocketDO pocket:pocketList){
            // 查询pocket内所有case
            List<PocketCaseDO> pocketCaseList = new LambdaQueryChainWrapper<>(pocketCaseMapper)
                    .eq(PocketCaseDO::getPocketId, pocket.getId())
                    .orderByAsc(PocketCaseDO::getPos)
                    .list();
            List<TreeBO> caseTreeList = new ArrayList<>(pocketCaseList.size());
            for (PocketCaseDO caseInfo:pocketCaseList){
                TreeBO childrenNode = new TreeBO();
                childrenNode.setId(caseInfo.getId());
                childrenNode.setBusinessId(caseInfo.getCaseId());
                childrenNode.setName(caseInfo.getCaseName());
                caseTreeList.add(childrenNode);
            }
            TreeBO pocketNode = new TreeBO();
            pocketNode.setId(pocket.getId());
            pocketNode.setName(pocket.getPocketName());
            pocketNode.setChildren(caseTreeList);
            pocketNode.setLevel(1);
            pocketTreeList.add(pocketNode);
        }

        return pocketTreeList;
    }

    @Override
    public List<HttpCaseDO> getCaseList(Integer collectionId) {
        // 查询所有pocket
        List<PocketDO> pocketList = new LambdaQueryChainWrapper<>(pocketMapper)
                .eq(PocketDO::getCollectionId, collectionId)
                .orderByAsc(PocketDO::getPos)
                .list();
        List<HttpCaseDO> caseList = new ArrayList<>();
        for (PocketDO pocket:pocketList) {
            // 查询pocket内所有case
            List<PocketCaseDO> pocketCaseList = new LambdaQueryChainWrapper<>(pocketCaseMapper)
                    .eq(PocketCaseDO::getPocketId, pocket.getId())
                    .orderByAsc(PocketCaseDO::getPos)
                    .list();
            for (PocketCaseDO pocketCaseDO:pocketCaseList){
                caseList.add(httpCaseService.getById(pocketCaseDO.getCaseId()));
            }
        }

        return caseList;
    }


    @Override
    public boolean insertCaseToPocket(PocketCaseDO pocketCaseDO) {
        Integer pos = getCasePos(pocketCaseDO.getPocketId());
        pocketCaseDO.setPos(pos);
        return pocketCaseMapper.insert(pocketCaseDO) > 0;

    }

    @Override
    public void batchInsertCaseToPocket(List<PocketCaseDO> list) {
        if (list.size() == 0){
            return;
        }
        AtomicReference<Integer> parentPos = new AtomicReference<>(getCasePos(list.get(0).getPocketId()));
        list.forEach( e->{
            e.setPos(parentPos.get());
            parentPos.updateAndGet(v -> v + 1024);
            pocketCaseMapper.insert(e);
        });
    }

    @Override
    public boolean deleteCaseFromPocket(PocketCaseDO pocketCaseDO) {

        LambdaQueryWrapper<PocketCaseDO> wrapper = new QueryWrapper<PocketCaseDO>().lambda();
        wrapper.eq(PocketCaseDO::getCaseId, pocketCaseDO.getCaseId())
                .eq(PocketCaseDO::getPocketId, pocketCaseDO.getPocketId());
        return pocketCaseMapper.delete(wrapper) > 0;
    }

    @Override
    public void batchDeleteCaseFromPocket(List<PocketCaseDO> list) {
        if (list.size() == 0){
            return;
        }
        list.forEach(this::deleteCaseFromPocket);
    }

    @Override
    public IPage<CollectionDO> searchCollectionList(SearchDTO searchDTO) {

        Page<CollectionDO> pager = new Page<>(searchDTO.getPage(), searchDTO.getCount());
        LambdaQueryWrapper<CollectionDO> wrapper = new QueryWrapper<CollectionDO>().lambda();
        wrapper.like(!searchDTO.getCollectionName().isEmpty(), CollectionDO::getCollectionName, searchDTO.getCollectionName())
                .eq(!searchDTO.getCreatorCode().isEmpty(), CollectionDO::getCreatorCode, searchDTO.getCreatorCode())
                .orderByDesc(CollectionDO::getId);
        return collectionMapper.selectPage(pager, wrapper);
    }

    @Override
    public PocketDO getPocket(Integer id) {
          return pocketMapper.selectById(id);
      }


    @Override
    public PocketCaseDO getPocketCase(Integer id) {
        return pocketCaseMapper.selectById(id);
    }


    @Override
    public void nodeDrag(List<Integer> ids) {
        Integer level = ids.get(0);
        // 获取相邻节点 id,这里注意在level=1的情况下拿到的是pocketId，在level=2时拿到的是caseId
        Integer before = ids.get(1);
        Integer current = ids.get(2);
        Integer after = ids.get(3);
        // pocket拖拽
        int pos;
        if (level == 1){
            PocketDO beforeNode = null;
            PocketDO afterNode = null;
            PocketDO currentNode = getPocket(current);
            // 获取相邻节点
            if (before != 0) {
                beforeNode = getPocket(before);
            }
            if (after != 0) {
                afterNode = getPocket(after);
            }
            if (beforeNode == null) {
                pos = afterNode != null ? afterNode.getPos() / 2 : 1024;
            } else {
                pos = afterNode != null ? (beforeNode.getPos() + afterNode.getPos()) / 2 : beforeNode.getPos() + 1024;
            }
            currentNode.setPos(pos);
            pocketMapper.updateById(currentNode);
        } else if (level == 2){
            PocketCaseDO beforeNode = null;
            PocketCaseDO afterNode = null;
            PocketCaseDO currentNode = getPocketCase(current);
            // 获取相邻节点
            if (before != 0) {
                beforeNode = getPocketCase(before);
            }
            if (after != 0) {
                afterNode = getPocketCase(after);
            }
            if (beforeNode == null) {
                pos = afterNode != null ? afterNode.getPos() / 2 : 1024;
            } else {
                pos = afterNode != null ? (beforeNode.getPos() + afterNode.getPos()) / 2 : beforeNode.getPos() + 1024;
            }
            currentNode.setPos(pos);
            pocketCaseMapper.updateById(currentNode);
        }

    }


    /**
     * 将CollectionDTO转为DO
     * @param collectionDTO
     * @return
     */
    private static CollectionDO collectionDtoToDo(CollectionDTO collectionDTO){
        CollectionDO collectionDO = new CollectionDO();
        BeanUtils.copyProperties(collectionDTO, collectionDO);
        if (collectionDTO.getPushList() != null){
            collectionDO.setPushList(String.join(",", collectionDTO.getPushList()));
        }
        return collectionDO;
    }

    /**
     * @param collectionId collectionId
     * @return 同collection下，下一个pocket的pos值
     */
    private Integer getPocketPos(Integer collectionId){
        List<PocketDO> list = new LambdaQueryChainWrapper<>(pocketMapper)
                .eq(PocketDO::getCollectionId, collectionId)
                .orderByDesc(PocketDO::getPos)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).getPos() + 1024;
        } else {
            return 1024;
        }
    }

    /**
     * @param pocketId pocketId
     * @return 同pocket下，下一个case的pos值
     */
    private Integer getCasePos(Integer pocketId){
        List<PocketCaseDO> list = new LambdaQueryChainWrapper<>(pocketCaseMapper)
                .eq(PocketCaseDO::getPocketId, pocketId)
                .orderByDesc(PocketCaseDO::getPos)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).getPos() + 1024;
        } else {
            return 1024;
        }
    }
}
