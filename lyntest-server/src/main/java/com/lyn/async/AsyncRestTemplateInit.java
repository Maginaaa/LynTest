package com.lyn.async;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * @author 简单随风
 * @date 2020/12/28
 */
@Component
public class AsyncRestTemplateInit {

    @Bean
    public AsyncRestTemplate asyncRestTemplate(){
        return new AsyncRestTemplate();
    }
}
