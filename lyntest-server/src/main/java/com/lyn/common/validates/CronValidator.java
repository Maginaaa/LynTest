package com.lyn.common.validates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 简单随风
 * @date 2020/12/16
 */
public class CronValidator implements ConstraintValidator<CronLength, String> {

    @Override
    public boolean isValid(String cron, ConstraintValidatorContext constraintValidatorContext) {
        if (cron == null) {
            return true;
        }
        if (cron.isEmpty()){
            return true;
        }
        return cron.split(" ").length == 6;
    }
}
