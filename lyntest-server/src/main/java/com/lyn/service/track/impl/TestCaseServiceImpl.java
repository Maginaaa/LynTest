package com.lyn.service.track.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.common.LocalUser;
import com.lyn.common.enumeration.TestCaseStatusEnum;
import com.lyn.dto.track.TestCaseSearchDTO;
import com.lyn.mapper.track.TestCaseMapper;
import com.lyn.model.UserDO;
import com.lyn.model.track.TestCaseDO;
import com.lyn.service.track.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-25
 */
@Service
@Transactional
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCaseDO> implements TestCaseService {

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Override
    public IPage<TestCaseDO> searchCaseList(TestCaseSearchDTO testCaseSearchDTO) {
        Page<TestCaseDO> pager = new Page<>(testCaseSearchDTO.getPage(), testCaseSearchDTO.getCount());
        LambdaQueryWrapper<TestCaseDO> wrapper = new QueryWrapper<TestCaseDO>().lambda();
        wrapper.eq(TestCaseDO::getProjectId, testCaseSearchDTO.getProjectId())
                .in(testCaseSearchDTO.getNodeIds().size() > 0, TestCaseDO::getNodeId,testCaseSearchDTO.getNodeIds())
                .like(!testCaseSearchDTO.getCaseName().isEmpty(), TestCaseDO::getName, testCaseSearchDTO.getCaseName())
                .orderByDesc(TestCaseDO::getId);
        return testCaseMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean createCase(TestCaseDO testCaseDO) {

        testCaseDO.setReviewStatus(TestCaseStatusEnum.Prepare.name());
        UserDO user = LocalUser.getLocalUser();
        testCaseDO.setCreatorCode(user.getCode());
        testCaseDO.setCreatorName(user.getUsername());
        testCaseDO.setCreateTime(new Date());
        testCaseDO.setUpdateCode(user.getCode());
        testCaseDO.setUpdateName(user.getUsername());
        testCaseDO.setUpdateTime(new Date());

        return testCaseMapper.insert(testCaseDO) > 0;
    }

    @Override
    public boolean editCase(TestCaseDO testCaseDO) {
        return testCaseMapper.updateById(testCaseDO) > 0;
    }

    @Override
    public boolean deleteCaseById(Integer id) {
        return testCaseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteCaseByNodeId(List<Integer> nodeIds) {
        LambdaQueryWrapper<TestCaseDO> wrapper = new QueryWrapper<TestCaseDO>().lambda();
        wrapper.in(TestCaseDO::getNodeId, nodeIds);
        return testCaseMapper.delete(wrapper) > 0;
    }
}
