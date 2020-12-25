package com.lyn.dto.autotest;

import com.lyn.dto.PageBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 简单随风
 * @date 2020/9/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchDTO extends PageBaseDTO {

    /**
     * case名称
     */
    private String caseName;

    /**
     * 所属分组
     */
    private String category;

    /**
     * 创建人code
     */
    private String creatorCode;

    /**
     * 集合名
     */
    private String collectionName;


}
