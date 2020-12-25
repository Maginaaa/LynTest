package com.lyn.dto.track;

import com.lyn.dto.PageBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author  简单随风
 * @date 2020/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectSearchDTO extends PageBaseDTO {

    private String name;

}
