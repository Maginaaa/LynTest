package com.lyn.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 *
 */
@NoArgsConstructor
@Data
public class UpdateInfoDTO {

    @Email(message = "{code}")
    private String code;

    @Length(min = 2, max = 10, message = "{username.length}")
    private String username;

    @Length(max = 500, message = "{avatar.length}")
    private String avatar;
}
