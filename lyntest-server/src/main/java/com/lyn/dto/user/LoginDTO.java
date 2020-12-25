package com.lyn.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author 
 */
@Data
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "{code.not-blank}")
    private String code;

    @NotBlank(message = "{password.new.not-blank}")
    private String password;
}
