package com.lyn.extension.wechat;

import com.lyn.common.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 熊林涛 739883
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
