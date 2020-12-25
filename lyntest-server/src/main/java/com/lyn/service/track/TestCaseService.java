package com.lyn.service.track;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.dto.track.TestCaseSearchDTO;
import com.lyn.model.track.TestCaseDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-25
 */
public interface TestCaseService extends IService<TestCaseDO> {

    /**
     * 获取Case列表
     * @return
     */
    IPage<TestCaseDO> searchCaseList(TestCaseSearchDTO testCaseSearchDTO);

    /**
     * 创建Case
     */
    boolean createCase(TestCaseDO testCaseDO);

    /**
     * 更新Case
     */
    boolean editCase(TestCaseDO testCaseDO);

    /**
     * 通过caseid删除Case
     */
    boolean deleteCaseById(Integer id);

    /**
     * 通过nodeId删除case
     * @param nodeIds
     * @return
     */
    boolean deleteCaseByNodeId(List<Integer> nodeIds);

}
