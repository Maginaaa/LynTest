package com.lyn.dto.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

/**
 *
 * @author 
 */
@Data
public class UpdateUserInfoDTO {

    private List<@Min(1) Integer> groupIds;
}
