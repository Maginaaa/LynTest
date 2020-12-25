package com.lyn.service.track;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.bo.TreeBO;
import com.lyn.model.track.TestCaseNodeDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-23
 */
public interface TestCaseNodeService extends IService<TestCaseNodeDO> {

    /**
     * 添加模块
     * @param testCaseNodeDO
     * @return
     */
    boolean addNode(TestCaseNodeDO testCaseNodeDO);

    /**
     * 修改模块
     * @param testCaseNodeDO
     * @return
     */
    boolean updateNode(TestCaseNodeDO testCaseNodeDO);

    /**
     * 批量删除模块
     * @param ids
     * @return
     */
    boolean deleteNodes(List<Integer> ids);


    void nodeDrag(TreeBO treeBO);

    boolean nodePosition(List<Integer> ids);

    /**
     * 模块查询
     */
    List<TreeBO> selectNodeTreeByProjectId(Integer projectId);

}
