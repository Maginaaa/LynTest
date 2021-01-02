package com.lyn.extension.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 简单随风
 * @date 2021/1/1
 */
@ConfigurationProperties(prefix = "wechat")
@Component
@Data
public class WeChatConfiguration {

    private String corpId;

    private Integer agentId;

    private String secret;

}
