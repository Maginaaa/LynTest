package com.lyn.service.autotest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.dto.autotest.PressureReportDTO;
import com.lyn.model.autotest.PressureReportDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-12-23
 */
public interface PressureReportService extends IService<PressureReportDO> {

    boolean saveReport(PressureReportDO pressureReportDO);

    PressureReportDTO getReportDetail(String id);

    List<String> getReportIdList(Integer caseId);

}
