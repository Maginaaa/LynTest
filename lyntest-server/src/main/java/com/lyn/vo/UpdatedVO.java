package com.lyn.vo;

import io.github.talelin.autoconfigure.bean.Code;
import com.lyn.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

/**
 *
 */
public class UpdatedVO extends UnifyResponseVO<String> {

    public UpdatedVO() {
        super(Code.UPDATED.getCode());
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public UpdatedVO(int code) {
        super(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public UpdatedVO(String message) {
        super(message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public UpdatedVO(int code, String message) {
        super(code, message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
