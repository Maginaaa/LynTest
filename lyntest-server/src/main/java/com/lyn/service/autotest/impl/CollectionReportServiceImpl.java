package com.lyn.service.autotest.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn.dto.autotest.CollectionReportDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.mapper.autotest.ReportMapper;
import com.lyn.model.autotest.CollectionReportDO;
import com.lyn.service.autotest.CollectionReportService;
import com.lyn.vo.AutoTestResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 简单随风
 * @date 2020/10/9
 */
@Service
@Transactional
public class CollectionReportServiceImpl implements CollectionReportService {

    @Autowired
    private ReportMapper reportMapper;


    @Override
    public CollectionReportDTO searchReport(String reportId) {
        CollectionReportDO collectionReportDO = reportMapper.selectById(reportId);
        if (collectionReportDO == null){
            return null;
        }
        return reportDoToDTO(collectionReportDO);
    }

    @Override
    public IPage<CollectionReportDO> getReportList(SearchDTO searchDTO) {
        // 查询出IPage<HttpCaseDO>
        Page<CollectionReportDO> pager = new Page<>(searchDTO.getPage(), searchDTO.getCount());
        LambdaQueryWrapper<CollectionReportDO> wrapper = new QueryWrapper<CollectionReportDO>().lambda();
        wrapper.eq(!searchDTO.getCollectionName().isEmpty(), CollectionReportDO::getCollectionName, searchDTO.getCollectionName())
                .orderByDesc(CollectionReportDO::getExecuteTime);
        return reportMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean saveNewReport(CollectionReportDO collectionReportDO) {

        return reportMapper.insert(collectionReportDO) > 0;
    }

    private static CollectionReportDTO reportDoToDTO(CollectionReportDO collectionReportDO){
        CollectionReportDTO collectionReportDTO = new CollectionReportDTO();
        BeanUtils.copyProperties(collectionReportDO, collectionReportDTO);
        List<AutoTestResponseVO> list = JSON.parseArray(collectionReportDO.getResult(), AutoTestResponseVO.class);
        // 排序放到前端进行处理
        // list.sort(Comparator.comparing(AutoTestResponseVO::getExpectedIsPass));
        collectionReportDTO.setResult(list);
        return collectionReportDTO;
    }
}
