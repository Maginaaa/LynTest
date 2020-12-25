//package com.lyn.common.redis;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.Topic;
//
///**
// * @author 简单随风
// * @date 2020/9/18
// * 使用键空间通知对redis进行监控
// */
//@Configuration
//public class MessageListenerConfiguration {
//
//    @Bean
//    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(redisConnection);
//        Topic topic = new PatternTopic("__keyevent@0__:*");
//
//        container.addMessageListener(new TopicMessageListener(), topic);
//        return container;
//    }
//}
