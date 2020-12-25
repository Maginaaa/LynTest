package com.lyn.service.track.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.common.LocalUser;
import com.lyn.common.enumeration.TestCaseReviewStatusEnum;
import com.lyn.dto.track.ReviewSearchDTO;
import com.lyn.dto.track.TestCaseReviewDTO;
import com.lyn.mapper.track.TestCaseReviewMapper;
import com.lyn.mapper.track.TestCaseReviewerMapper;
import com.lyn.model.track.TestCaseReviewDO;
import com.lyn.model.track.TestCaseReviewerDO;
import com.lyn.service.track.TestCaseReviewService;
import org.springframework.beans.BeanUtils;
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
 * @since 2020-12-14
 */
@Service
@Transactional
public class TestCaseReviewServiceImpl extends ServiceImpl<TestCaseReviewMapper, TestCaseReviewDO> implements TestCaseReviewService {

    @Autowired
    private TestCaseReviewMapper testCaseReviewMapper;

    @Autowired
    private TestCaseReviewerMapper testCaseReviewerMapper;

    @Override
    public IPage<TestCaseReviewDO> searchReviewList(ReviewSearchDTO reviewSearchDTO) {
        Page<TestCaseReviewDO> pager = new Page<>(reviewSearchDTO.getPage(), reviewSearchDTO.getCount());
        LambdaQueryWrapper<TestCaseReviewDO> wrapper = new QueryWrapper<TestCaseReviewDO>().lambda();
        wrapper.like(!reviewSearchDTO.getName().isEmpty(), TestCaseReviewDO::getName, reviewSearchDTO.getName())
                .orderByDesc(TestCaseReviewDO::getId);

        return testCaseReviewMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean createTestCaseReview(TestCaseReviewDTO testCaseReviewDTO) {

        TestCaseReviewDO testCaseReviewDO = new TestCaseReviewDO();
        BeanUtils.copyProperties(testCaseReviewDTO, testCaseReviewDO);
        // 先创建用例评审基础信息
        testCaseReviewDO.setCreateTime(new Date());
        testCaseReviewDO.setCreator(LocalUser.getLocalUser().getUsername());
        testCaseReviewDO.setStatus(TestCaseReviewStatusEnum.Prepare.name());
        testCaseReviewMapper.insert(testCaseReviewDO);
        // 保存评审人员信息
        Integer reviewId = testCaseReviewDO.getId();
        List<String> reviewerCode = testCaseReviewDTO.getReviewer();
        reviewerCode.forEach( e->{
            testCaseReviewerMapper.insert(TestCaseReviewerDO.builder().reviewId(reviewId).userCode(e).build());
        });

        return reviewId > 0;
    }

    @Override
    public boolean editTestCaseReview(TestCaseReviewDTO testCaseReviewDTO) {

        TestCaseReviewDO testCaseReviewDO = new TestCaseReviewDO();
        BeanUtils.copyProperties(testCaseReviewDTO, testCaseReviewDO);
        Integer reviewId = testCaseReviewDO.getId();
        List<String> reviewerCode = testCaseReviewDTO.getReviewer();
        testCaseReviewerMapper.deleteBatchIds(reviewerCode);
        reviewerCode.forEach( e->{
            testCaseReviewerMapper.insert(TestCaseReviewerDO.builder().reviewId(reviewId).userCode(e).build());
        });
        return testCaseReviewMapper.updateById(testCaseReviewDO) > 0;
    }

    @Override
    public boolean deleteReviewById(Integer id) {
        return false;
    }
}
