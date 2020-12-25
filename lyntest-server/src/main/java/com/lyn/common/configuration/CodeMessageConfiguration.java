package com.lyn.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 简单随风
 */
@SuppressWarnings("ConfigurationProperties")
@Component
@ConfigurationProperties
@PropertySource(value = "classpath:code-message.properties", encoding = "UTF-8")
public class CodeMessageConfiguration {

    private static Map<Integer, String> codeMessage = new HashMap<>();

    public static String getMessage(Integer code) {
        return codeMessage.get(code);
    }

    public Map<Integer, String> getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(Map<Integer, String> codeMessage) {
        CodeMessageConfiguration.codeMessage = codeMessage;
    }
}
