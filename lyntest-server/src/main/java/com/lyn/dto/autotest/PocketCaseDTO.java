package com.lyn.dto.autotest;

import com.lyn.model.autotest.PocketCaseDO;
import lombok.Data;

import java.util.List;

/**
 * @author 简单随风
 * @date 2020/9/24
 */
@Data
public class PocketCaseDTO {

    /**
     * 集合id
     */
    private Integer pocketId;

    /**
     * 集合名
     */
    private String pocketName;

    /**
     * 包内case
     */
    private List<PocketCaseDO> caseList;


}
