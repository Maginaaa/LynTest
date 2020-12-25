package com.lyn.dto.track;

import com.lyn.dto.PageBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author  简单随风
 * @date 2020/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestCaseSearchDTO extends PageBaseDTO {

    @NotNull
    private Integer projectId;

    private List<Integer> nodeIds;

    /**
     * 测试用例名
     */
    private String caseName;

}
