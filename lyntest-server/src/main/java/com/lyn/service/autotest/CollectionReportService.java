package com.lyn.service.autotest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyn.dto.autotest.CollectionReportDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.model.autotest.CollectionReportDO;

/**
 * @author 简单随风
 * @date 2020/10/9
 */
public interface CollectionReportService {

    /**
     * 查询测试报告
     */
    CollectionReportDTO searchReport(String reportId);

    /**
     * 测试报告列表
     */
    IPage<CollectionReportDO> getReportList(SearchDTO searchDTO);

    /**
     * 保存新的测试报告
     * @param collectionReportDO
     * @return
     */
    boolean saveNewReport(CollectionReportDO collectionReportDO);
}
