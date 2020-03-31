package com.lyntest.service;

import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.User;


public interface UserLoginService {

    User userLogin(User user);

    ResponseVo userRegistered(User user);

}
