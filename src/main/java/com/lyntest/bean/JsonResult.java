package com.lyntest.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult<T> {

    public static final String SUCCESS = "200";

    public static final String FAILURE = "501";

    public static final String ERROR = "500";

    private String status;

    private T body;

    private String message;

    public JsonResult(String status, T body) {
        this.status = status;
        this.body = body;
    }

    public static <T> JsonResult<T> success(T body, String msg) {
        return new JsonResult<>(SUCCESS, body, msg);
    }

    public static <T> JsonResult<T> failure(T body, String msg) {
        return new JsonResult<>(FAILURE, body, msg);
    }

    public static <T> JsonResult<T> error(T body, String msg) {
        return new JsonResult<>(ERROR, body, msg);
    }

    public static <T> JsonResult<T> success(T body) {
        return new JsonResult<T>(SUCCESS, body);
    }

    public static <T> JsonResult<T> failure(T body) {
        return new JsonResult<T>(FAILURE, body);
    }

    public static <T> JsonResult<T> error(T body) {
        return new JsonResult<T>(ERROR, body);
    }

}