package com.lyn.extension.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyn.async.AutoTestConfig;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 简单随风
 * @date 2021/1/1
 */
@Component
public class WeChatPush {

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(AutoTestConfig.connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(AutoTestConfig.writeTimeout,TimeUnit.SECONDS)
            .readTimeout(AutoTestConfig.readTimeout, TimeUnit.SECONDS)
            .build();

    @Autowired
    private WeChatConfiguration weChatConfiguration;

    public String getAccessToken(){

        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
                weChatConfiguration.getCorpId(),weChatConfiguration.getSecret());

        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject object = JSONObject.parseObject(response.body().string());
            return object.getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean messagePush(String content){

        String token = this.getAccessToken();
        if (token == null){
            return false;
        }
        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s",token);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", "@all");
        jsonObject.put("msgtype", "text");
        jsonObject.put("agentid", weChatConfiguration.getAgentId());
        JSONObject text = new JSONObject();
        text.put("content", content);
        jsonObject.put("text", text);
        RequestBody requestBody =RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(jsonObject));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject object = JSONObject.parseObject(response.body().string());
            return "ok".equals(object.getString("errmsg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }


}
