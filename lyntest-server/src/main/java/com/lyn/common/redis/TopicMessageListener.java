//package com.lyn.common.redis;
//
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//
///**
// * @author 简单随风
// * @date 2020/9/18
// */
//public class TopicMessageListener implements MessageListener {
//
//    @Override
//    public void onMessage(Message message, byte[] bytes) {
//        byte[] body = message.getBody();
//        byte[] channel = message.getChannel();
//
//        String key = new String(body);
//        String topic = new String(channel);
//        System.out.println(key);
//        System.out.println(topic);
//
//        //TODO:获取通知后的业务代码
//
//    }
//}
