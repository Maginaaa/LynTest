package com.lyn.service.track;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.dto.track.ReviewSearchDTO;
import com.lyn.dto.track.TestCaseReviewDTO;
import com.lyn.model.track.TestCaseReviewDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-12-14
 */
public interface TestCaseReviewService extends IService<TestCaseReviewDO> {

    /**
     * 获取Case列表
     * @return
     */
    IPage<TestCaseReviewDO> searchReviewList(ReviewSearchDTO reviewSearchDTO);

    /**
     * 创建Case
     */
    boolean createTestCaseReview(TestCaseReviewDTO testCaseReviewDTO);

    /**
     * 更新Case
     */
    boolean editTestCaseReview(TestCaseReviewDTO testCaseReviewDTO);

    /**
     * 通过caseid删除Case
     */
    boolean deleteReviewById(Integer id);

}
