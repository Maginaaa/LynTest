package com.lyn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 简单随风
 */
@Data
@Accessors(chain=true)
public class ResponseVO {

    private Boolean success;

    private Object result;

    private Object msg;

    public ResponseVO(){}

    public ResponseVO(Boolean success, Object result, Object msg){
        this.success = success;
        this.result = result;
        this.msg = msg;
    }

    public static ResponseVO success(){
        return new ResponseVO(Boolean.TRUE, null, null);
    }

    public static ResponseVO success(Object result){
        return new ResponseVO(Boolean.TRUE, result, null);
    }

    public static ResponseVO success(Object result, Object msg){
        return new ResponseVO(Boolean.TRUE, result, msg);
    }

    public static ResponseVO failure() {
        return new ResponseVO(Boolean.FALSE, null, null);
    }

    public static ResponseVO failure(Object msg){
        return new ResponseVO(Boolean.FALSE, null, msg);
    }


}
