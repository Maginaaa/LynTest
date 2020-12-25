package com.lyn.dto.admin;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 *  
 */
@Data
public class UpdateGroupDTO {

    @NotBlank(message = "{group.name.not-blank}")
    @Length(min = 1, max = 60, message = "{group.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{group.info.length}")
    private String info;
}
