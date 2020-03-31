package com.lyntest.service.impl;

import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.User;
import com.lyntest.mapper.UserMapper;
import com.lyntest.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service(value = "UserLoginService")
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户工号,密码查询用户基础信息
     */
    @Override
    public User userLogin(User user){

        User u =  userMapper.selectUserByCode(user.getCode());
        if (u.getPassword().equals(user.getPassword())){
            return  u;
        }else {
            return null;
        }
    }

    @Override
    public ResponseVo userRegistered(User user) {

        ResponseVo responseVo = new ResponseVo();

        User u =  userMapper.selectUserByCode(user.getCode());
        if(u == null){
            Integer i = userMapper.userRegistered(user);
            if (i == 1){
                responseVo.setIsSuccess(Boolean.TRUE);
                responseVo.setResult("注册成功");
            }
        } else {
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("用户已注册");
        }
        return responseVo;
    }


}
