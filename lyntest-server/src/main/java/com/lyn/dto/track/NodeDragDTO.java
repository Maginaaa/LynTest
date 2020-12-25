package com.lyn.dto.track;

import com.lyn.bo.TreeBO;
import com.lyn.model.track.TestCaseNodeDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author  简单随风
 * @date 2020/11/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NodeDragDTO extends TestCaseNodeDO {

    List<Integer> childIds;

    TreeBO nodeTree;

}
