package com.lyn.controller.autotest;


import com.lyn.async.CollectionExecute;
import com.lyn.bo.TreeBO;
import com.lyn.common.LocalUser;
import com.lyn.common.util.PageUtil;
import com.lyn.dto.autotest.CollectionDTO;
import com.lyn.dto.autotest.CollectionReportDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.model.UserDO;
import com.lyn.model.autotest.CollectionDO;
import com.lyn.model.autotest.CollectionReportDO;
import com.lyn.model.autotest.PocketCaseDO;
import com.lyn.model.autotest.PocketDO;
import com.lyn.service.autotest.CollectionReportService;
import com.lyn.service.autotest.CollectionService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;
import io.github.talelin.core.annotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

/**
*  @author 简单随风
* @since 2020-09-18
*/
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private CollectionExecute collectionExecute;

    @Autowired
    private CollectionReportService collectionReportService;

    /**
     * 新增集合
     * @param collectionDTO
     * @return
     */
    @PostMapping("/create")
    @LoginRequired
    public CreatedVO create(@RequestBody @Valid CollectionDTO collectionDTO) {
        UserDO user = LocalUser.getLocalUser();
        collectionDTO.setCreatorCode(user.getCode());
        collectionDTO.setCreatorName(user.getUsername());
        int collectionId = collectionService.createCollection(collectionDTO);
        return new CreatedVO(String.valueOf(collectionId));
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody @Valid CollectionDTO collectionDTO) {
        collectionExecute.collectionTimingTask(collectionDTO);
        collectionService.updateCollection(collectionDTO);
        return new UpdatedVO();
    }


    @GetMapping("/{id}")
    public CollectionDTO get(@PathVariable(value = "id") @Positive(message = "{id.positive}") Integer id) {
        return collectionService.getCollectionDetail(id);
    }

    @PostMapping("/list")
    public PageResponseVO<CollectionDO> collectionList(@RequestBody @Valid SearchDTO searchDTO) {
        return PageUtil.build(collectionService.searchCollectionList(searchDTO));
    }

    @GetMapping("/pocket/{id}")
    public List<TreeBO> getPocketInfo(@PathVariable(value = "id") @Positive(message = "{id.positive}") Integer id) {
        return collectionService.getCaseTree(id);
    }

    /**
     * 创建pocket
     * @param pocketDO
     */
    @PostMapping("/pocket/create")
    public CreatedVO pocketCreate(@RequestBody PocketDO pocketDO) {
        Integer i= collectionService.createPocket(pocketDO);
        return new CreatedVO(String.valueOf(i));
    }

    /**
     * 创建pocket
     * @param pocketDO
     */
    @PostMapping("/pocket/update")
    public UpdatedVO pocketUpdate(@RequestBody PocketDO pocketDO) {
        collectionService.updatePocket(pocketDO);
        return new UpdatedVO();
    }

    @DeleteMapping("/pocket/{id}")
    public DeletedVO update(@PathVariable Integer id) {
        collectionService.deletePocket(id);
        return new DeletedVO();
    }

    /**
     * pocket中创建case
     * @param pocketCaseDO
     */
    @PostMapping("/case/create")
    public CreatedVO caseCreate(@RequestBody PocketCaseDO pocketCaseDO) {
        collectionService.insertCaseToPocket(pocketCaseDO);
        return new CreatedVO();
    }

    /**
     * pocket中批量创建case
     * @param list
     */
    @PostMapping("/case/batchCreate")
    public CreatedVO caseBatchCreate(@RequestBody List<PocketCaseDO> list) {
        collectionService.batchInsertCaseToPocket(list);
        return new CreatedVO();
    }


    /**
     * pocket中删除case
     * @param pocketCaseDO
     */
    @PostMapping("/case/delete")
    public DeletedVO caseDelete(@RequestBody PocketCaseDO pocketCaseDO) {
        collectionService.deleteCaseFromPocket(pocketCaseDO);
        return new DeletedVO();
    }

    /**
     * pocket中批量创建case
     * @param list
     */
    @PostMapping("/case/batchDelete")
    public DeletedVO caseBatchDelete(@RequestBody List<PocketCaseDO> list) {
        collectionService.batchDeleteCaseFromPocket(list);
        return new DeletedVO();
    }

    /**
     * node拖拽
     * @param ids
     * @return
     */
    @PostMapping("/position")
    public UpdatedVO nodePosition(@RequestBody List<Integer> ids) {
        collectionService.nodeDrag(ids);
        return new UpdatedVO();
    }

    /**
     * 获取测试报告
     * @param reportId 测试报告id
     * @return 测试报告的详情 或 null
     */
    @GetMapping("/report/{reportId}")
    public CollectionReportDTO get(@PathVariable String reportId) {
        return collectionReportService.searchReport(reportId);
    }

    @PostMapping("/report/list")
    public PageResponseVO<CollectionReportDO> reportList(@RequestBody @Validated SearchDTO searchDTO) {

        return PageUtil.build(collectionReportService.getReportList(searchDTO));
    }


    /**
     * @param collectionId 集合id
     * @return 测试报告id
     */
    @PostMapping("/execute/{collectionId}")
    @LoginRequired
    public String execute(@PathVariable @Positive(message = "{id.positive}") Integer collectionId) {

        CollectionReportDO collectionReportDO = new CollectionReportDO();
        collectionReportDO.setCollectionId(collectionId);

        UserDO user = LocalUser.getLocalUser();
        collectionReportDO.setExecuteTime(new Date());
        collectionReportDO.setExecuteCode(user.getCode());
        collectionReportDO.setExecuteName(user.getUsername());
        String reportId = collectionId + "_" + System.currentTimeMillis();
        collectionReportDO.setId(reportId);

        collectionExecute.execute(collectionReportDO);
        return reportId;
    }

}
