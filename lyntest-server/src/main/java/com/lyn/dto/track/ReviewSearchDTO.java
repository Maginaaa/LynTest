package com.lyn.dto.track;

import com.lyn.dto.PageBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author  简单随风
 * @date 2020/12/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewSearchDTO extends PageBaseDTO {

    private String name;
}
