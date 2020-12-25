package com.lyn.dto.mock;

import com.lyn.dto.PageBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 简单随风
 * @date 2020/9/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MockSearchDTO extends PageBaseDTO {

    private String path;
}
