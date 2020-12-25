package com.lyn.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 简单随风
 * @date 2020/7/27
 */
public class FormatValid {

    /**
     * 判断是否为jsonObject
     * @param content
     * @return
     */
    public static Boolean isJsonObject(String content){
        if (content.isEmpty()){
            return false;
        }
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
