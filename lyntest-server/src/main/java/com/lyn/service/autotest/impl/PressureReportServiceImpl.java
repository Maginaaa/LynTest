package com.lyn.service.autotest.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.dto.autotest.PressureReportDTO;
import com.lyn.mapper.autotest.PressureReportMapper;
import com.lyn.model.autotest.PressureReportDO;
import com.lyn.service.autotest.PressureReportService;
import com.lyn.vo.AutoTestResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-12-23
 */
@Service
@Transactional
public class PressureReportServiceImpl extends ServiceImpl<PressureReportMapper, PressureReportDO> implements PressureReportService {

    @Autowired
    private PressureReportMapper pressureReportMapper;

    @Override
    public boolean saveReport(PressureReportDO pressureReportDO) {
        return pressureReportMapper.insert(pressureReportDO) > 0;
    }

    @Override
    public PressureReportDTO getReportDetail(String id) {
        PressureReportDO pressureReportDO = pressureReportMapper.selectById(id);
        if (pressureReportDO == null){
            return null;
        }
        return reportDoToDTO(pressureReportDO);
    }

    @Override
    public List<String> getReportIdList(Integer caseId) {
        LambdaQueryWrapper<PressureReportDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PressureReportDO::getId).eq(PressureReportDO::getCaseId, caseId).orderByDesc(PressureReportDO::getId);
        return pressureReportMapper.selectList(wrapper).stream().map(PressureReportDO::getId).collect(Collectors.toList());
    }

    private static PressureReportDTO reportDoToDTO(PressureReportDO pressureReportDO){
        PressureReportDTO pressureReportDTO = new PressureReportDTO();
        BeanUtils.copyProperties(pressureReportDO, pressureReportDTO);
        List<AutoTestResponseVO> list = JSON.parseArray(pressureReportDO.getResult(), AutoTestResponseVO.class);
         list.sort(Comparator.comparing(AutoTestResponseVO::getExpectedIsPass));
        pressureReportDTO.setResult(list);
        return pressureReportDTO;
    }
}
