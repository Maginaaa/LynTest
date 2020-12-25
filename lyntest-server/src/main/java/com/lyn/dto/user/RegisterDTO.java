package com.lyn.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 *
 * @author 简单随风
 */
@Data
@NoArgsConstructor
@EqualField(srcField = "password", dstField = "confirmPassword", message = "{password.equal-field}")
public class RegisterDTO {

    @NotBlank(message = "{username.not-blank}")
    @Length(min = 2, max = 10, message = "{username.length}")
    private String username;

    private List<Integer> groupIds;

    // 工号正则可根据实际需求自行修改
    @NotBlank(message = "{code.not-blank}")
    @Pattern(regexp = ".*?", message = "{code.pattern}")
    private String code;

    @NotBlank(message = "{password.new.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@!]{6,22}$", message = "{password.new.pattern}")
    private String password;

    @NotBlank(message = "{password.confirm.not-blank}")
    private String confirmPassword;
}
