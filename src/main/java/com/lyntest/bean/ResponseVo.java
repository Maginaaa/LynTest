package com.lyntest.bean;

import lombok.Data;

@Data
public class ResponseVo {

    private Boolean isSuccess;

    private Object result;

    private Object msg;


}
