package com.lyn.common.validates;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author 简单随风
 * @date 2020/12/16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CronValidator.class)
public @interface CronLength {

    // 校验未通过的时的返回信息
    String message() default "cron只支持6位长度";

    // 以下两行为固定模板
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
