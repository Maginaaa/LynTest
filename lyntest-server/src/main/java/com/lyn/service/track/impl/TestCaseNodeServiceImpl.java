package com.lyn.service.track.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.bo.TreeBO;
import com.lyn.mapper.track.TestCaseNodeMapper;
import com.lyn.model.track.TestCaseNodeDO;
import com.lyn.service.track.TestCaseNodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-23
 */
@Service
@Transactional
public class TestCaseNodeServiceImpl extends ServiceImpl<TestCaseNodeMapper, TestCaseNodeDO> implements TestCaseNodeService {

    @Autowired
    private TestCaseNodeMapper testCaseNodeMapper;

    @Override
    public boolean addNode(TestCaseNodeDO node) {
        node.setCreateTime(new Date());
        node.setUpdateTime(new Date());
        Integer pos = getNextLevelPos(node.getProjectId(), node.getLevel(), node.getParentId());
        node.setPos(pos);
        return testCaseNodeMapper.insert(node) > 0;
    }

    @Override
    public boolean updateNode(TestCaseNodeDO node) {
        node.setUpdateTime(new Date());
        return testCaseNodeMapper.updateById(node) > 0;
    }

    @Override
    public boolean deleteNodes(List<Integer> ids) {
        return testCaseNodeMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void nodeDrag(TreeBO nodeTree) {

        List<TestCaseNodeDO> updateNodes = new ArrayList<>();
        buildUpdateTestCase(nodeTree, updateNodes, 0, 1);

        updateNodes.forEach( node -> {
            testCaseNodeMapper.updateById(node);
        });
    }

    @Override
    public boolean nodePosition(List<Integer> ids) {
        // 获取相邻节点 id
        Integer before = ids.get(0);
        Integer current = ids.get(1);
        Integer after = ids.get(2);

        TestCaseNodeDO beforeCase = null;
        TestCaseNodeDO afterCase = null;

        TestCaseNodeDO currentNode = getCaseNode(current);
        // 获取相邻节点
        if (before != 0) {
            beforeCase = getCaseNode(before);
            beforeCase = beforeCase.getLevel().equals(currentNode.getLevel()) ? beforeCase : null;
        }

        if (after != 0) {
            afterCase = getCaseNode(after);
            afterCase = afterCase.getLevel().equals(currentNode.getLevel()) ? afterCase : null;
        }

        int pos;

        if (beforeCase == null) {
            pos = afterCase != null ? afterCase.getPos() / 2 : 1024;
        } else {
            pos = afterCase != null ? (beforeCase.getPos() + afterCase.getPos()) / 2 : beforeCase.getPos() + 1024;
        }
        currentNode.setPos(pos);
        return testCaseNodeMapper.updateById(currentNode) > 0;
    }

    private void buildUpdateTestCase(TreeBO rootNode, List<TestCaseNodeDO> updateNodes, Integer parentId, int level) {

        TestCaseNodeDO testCaseNode = new TestCaseNodeDO();
        testCaseNode.setId(rootNode.getId());
        testCaseNode.setLevel(level);
        testCaseNode.setParentId(parentId);
        testCaseNode.setPos(rootNode.getPos());
        updateNodes.add(testCaseNode);

        List<TreeBO> children = rootNode.getChildren();
        if (children != null && children.size() > 0) {
            for (TreeBO child : children) {
                buildUpdateTestCase(child, updateNodes, rootNode.getId(), level + 1);
            }
        }
    }

    @Override
    public List<TreeBO> selectNodeTreeByProjectId(Integer projectId) {
        List<TestCaseNodeDO> nodeList = new LambdaQueryChainWrapper<>(testCaseNodeMapper)
                .eq(TestCaseNodeDO::getProjectId, projectId)
                .orderByAsc(TestCaseNodeDO::getPos)
                .list();

        List<TreeBO> treeList = new ArrayList<>();
        // 将单层的TestCaseNodeDO变成以level为key的多层map, [A1,A2,A3,B1]-> {1:[A1,B1],2:[A2],3:[A3]}
        Map<Integer, List<TestCaseNodeDO>> nodeLevelMap = new HashMap<>();
        nodeList.forEach(node -> {
            Integer level = node.getLevel();
            if (nodeLevelMap.containsKey(level)) {
                nodeLevelMap.get(level).add(node);
            } else {
                List<TestCaseNodeDO> testCaseNodes = new ArrayList<>();
                testCaseNodes.add(node);
                nodeLevelMap.put(node.getLevel(), testCaseNodes);
            }
        });

        List<TestCaseNodeDO> rootNodes = Optional.ofNullable(nodeLevelMap.get(1)).orElse(new ArrayList<>());
        rootNodes.forEach(rootNode -> treeList.add(buildNodeTree(nodeLevelMap, rootNode)));

        return treeList;
    }

    private TestCaseNodeDO getCaseNode(Integer id) {
        return testCaseNodeMapper.selectById(id);
    }



    /**
     * 递归构建节点树
     * @param nodeLevelMap
     * @param rootNode
     * @return
     */
    private TreeBO buildNodeTree(Map<Integer, List<TestCaseNodeDO>> nodeLevelMap, TestCaseNodeDO rootNode) {

        TreeBO nodeTree = new TreeBO();
        BeanUtils.copyProperties(rootNode, nodeTree);
        nodeTree.setName(rootNode.getName());

        List<TestCaseNodeDO> lowerNodes = nodeLevelMap.get(rootNode.getLevel() + 1);
        if (lowerNodes == null) {
            return nodeTree;
        }

        List<TreeBO> children = Optional.ofNullable(nodeTree.getChildren()).orElse(new ArrayList<>());

        lowerNodes.forEach(node -> {
            if (node.getParentId() != null && node.getParentId().equals(rootNode.getId())) {
                children.add(buildNodeTree(nodeLevelMap, node));
                nodeTree.setChildren(children);
            }
        });

        return nodeTree;
    }


    /**
     * 获得同级模块下一个 pos 值
     * @param projectId project id
     * @param level node level
     * @param parentId node parent id
     * @return 同级模块下一个 pos 值
     */
    private Integer getNextLevelPos(Integer projectId, Integer level, Integer parentId) {
        List<TestCaseNodeDO> list = new LambdaQueryChainWrapper<>(testCaseNodeMapper)
                .eq(TestCaseNodeDO::getProjectId, projectId)
                .eq(TestCaseNodeDO::getLevel, level)
                .eq(parentId != null, TestCaseNodeDO::getParentId, parentId)
                .orderByDesc(TestCaseNodeDO::getPos)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).getPos() + 1024;
        } else {
            return 1024;
        }
    }
}
