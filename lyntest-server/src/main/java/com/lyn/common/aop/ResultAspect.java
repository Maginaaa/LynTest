package com.lyn.common.aop;

import cn.hutool.core.util.StrUtil;
import com.lyn.common.configuration.CodeMessageConfiguration;
import com.lyn.vo.UnifyResponseVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 处理返回结果为 UnifyResponseVO 的 Controller
 * message 默认为 null，在此处通过 code 设置为对应消息
 *
 * @author 简单随风
 */
@Aspect
@Component
public class ResultAspect {
    @AfterReturning(returning = "result", pointcut = "execution(public * com.lyn.controller..*.*(..))")
    public void doAfterReturning(UnifyResponseVO<String> result) {
        int code = result.getCode();
        String oldMessage = result.getMessage();
        // code-message.properties 中配置的 message
        String newMessage = CodeMessageConfiguration.getMessage(code);

        // 如果 code-message.properties 中指定了相应的 message 并且 UnifyResponseVO 的 message 为null
        // 则使用 newMessage 替换 oldMessage
        if (StrUtil.isNotBlank(newMessage) && StrUtil.isBlank(oldMessage)) {
            result.setMessage(newMessage);
        }
    }
}
