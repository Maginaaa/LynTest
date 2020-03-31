package com.lyntest.controller;


import com.alibaba.fastjson.JSONObject;
import com.lyntest.bean.JsonResult;
import com.lyntest.bean.ResponseVo;
import com.lyntest.bean.TokenObject;
import com.lyntest.bean.User;
import com.lyntest.service.UserLoginService;
import com.lyntest.utils.AuthUtil;
import com.lyntest.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 登录
 */
@Slf4j
@RestController
@RequestMapping()
public class LoginController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 用户登录请求,不走拦截
     * @return
     */
    @PostMapping(value = "/login")
    public Object applogin(@RequestBody User user) {
        long startTime = System.currentTimeMillis();
        StringBuffer logstr = new StringBuffer("===== login start ,start time :" + startTime);
        TokenObject tokenObject;

        try {

            User userObj = userLoginService.userLogin(user);

            if(null==userObj){
                return JsonResult.failure(null, "用户密码错误!");
            }

            // 构造Token对象
            tokenObject = new TokenObject();
            String accessToken = JwtHelper.createJWTByObj(userObj);
            tokenObject.setEmpCode(userObj.getCode());
            tokenObject.setName(userObj.getName());
            tokenObject.setAccessToken(accessToken);

            logstr.append(" access token time: " + (System.currentTimeMillis() - startTime));
            // 将token 存放到缓存中 ,JwtHelper.EXPIRATION/1000 JwtHelper.EXPIRATION/1000
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(logstr.append(" login error end time :" + (System.currentTimeMillis() - startTime)).toString());
            return JsonResult.error(e, "系统异常!");
        }

        logstr.append(" user loing end time:" + (System.currentTimeMillis() - startTime));
        log.info(logstr.toString());
        // 此处调用密码解密方法
        return JsonResult.success(tokenObject, null);
    }


    /** 注册 */
    @PostMapping(value = "/registered")
    public ResponseVo doRegistered(@RequestBody User user) {

        return userLoginService.userRegistered(user);
    }

    /**
     * 获取当前登录用户名
     */
    @GetMapping(value = "/username")
    public ResponseVo getUserName(){

        ResponseVo responseVo = new ResponseVo();

        JSONObject jsonObject = new JSONObject();

        String name = AuthUtil.getTokenUserInfo().getName();
        jsonObject.put("userName", name);

        responseVo.setResult(jsonObject);

        return responseVo;
    }



    /**
     * 退出登录
     *
     */
//    @RequestMapping(value = "/user/{username}/exitLogin", method = RequestMethod.POST)
//    public Object exitLogon(@PathVariable("username") String username) {
//        try {
//            // 清除缓存记录
//            log.info("用户:" + username + " 退出登录!");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return JsonResult.success(null, "退出登录成功!");
//    }

}
