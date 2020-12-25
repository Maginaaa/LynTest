package com.lyn.bo;

import lombok.Data;
import okhttp3.Response;

/**
 * @author 简单随风
 * @date 2020/9/11
 */
@Data
public class ResponseBO {

    private Boolean success;

    private Response response;

    private String msg;

    public ResponseBO(Boolean success, Response response, String msg){
        this.response = response;
        this.success = success;
        this.msg = msg;
    }

    public static ResponseBO success(Response response) {
        return new ResponseBO(Boolean.TRUE, response, null);
    }

    public static ResponseBO failure(String msg) {
        return new ResponseBO(Boolean.FALSE, null, msg);
    }

}
