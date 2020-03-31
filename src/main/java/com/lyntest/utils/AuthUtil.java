package com.lyntest.utils;

import com.lyntest.bean.User;
import com.lyntest.filter.HttpBasicAuthorizeAttribute;

import javax.servlet.http.HttpServletRequest;

public class AuthUtil {

    public static String getClinetIdByAuth(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        auth = auth.substring(7);
        return JwtHelper.getIdByJWT(auth);
    }

    /**
     * 获取用户信息
     * @return
     */
    public static User getTokenUserInfo(){

        return HttpBasicAuthorizeAttribute.getTokenUserInfo().get();
    }
}
