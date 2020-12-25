package com.lyn.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 *
 * @author 
 */
@Data
public class DispatchPermissionDTO {

    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Integer groupId;

    @Positive(message = "{permission.id.positive}")
    @NotNull(message = "{permission.id.not-null}")
    private Integer permissionId;
}
