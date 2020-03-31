package com.lyntest.utils;


import com.alibaba.fastjson.JSONObject;
import com.lyntest.bean.TokenObject;
import com.lyntest.bean.User;
import com.lyntest.config.LoginConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

public class JwtHelper {

    private static Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    /**
     * "dataplatform"经过MD5加密的值
     */
    private static final String getMD5Secret = "F7BE2E40CFFA0199213D3A7EA133E0F3";



    /**
     * 解析JWT
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security){
        try
        {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * 从Token 获取用户对象信息
     * @param token
     * @return
     */
    public static User getUsername(String token){
        Claims c = getTokenBody(token, getMD5Secret);
        String userSter = c.get("userInfo").toString();
        User user = JSONObject.parseObject(userSter, User.class);
        return user;
    }



    /**
     * 获取用户code
     */
    public static String  getIdByJWT(String jwt){
        if (jwt != null) {
            if (jwt.split("\\.").length == 3) {
                logger.info("jwt:" + jwt);
                String[] split = jwt.split("\\.");
                String content = split[1];
                String s = Base64Codec.BASE64URL.decodeToString(content);
                JSONObject jsonObject1 = JSONObject.parseObject(s);
                TokenObject o = JSONObject.toJavaObject(jsonObject1, TokenObject.class);
                System.out.println(o.getName());
                return o.getEmpCode();
            }
        }
        return "";
    }


    /**
     * 创建jwt
     * @return
     */
    public static String createJWTByObj(User user) {
        // 获取当前时间戳
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        User u = new User();
        u.setCode(user.getCode());
        u.setName(user.getName());


        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签名密钥 服务器自己定义的秘钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(getMD5Secret);
        SecretKeySpec signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("userInfo", JSONObject.toJSONString(u))
                .signWith(signatureAlgorithm, signingKey);

        // 设置token过期时间
        long expMillis = nowMillis + LoginConfig.JWT_EXPIRES_SECOND;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

        //生成JWT
        return builder.compact();
    }


    /**
     * 判断Token 是否过期
     * @param token
     * @return
     */
    public static boolean isExpiration(String token){
        boolean tag = true;
        try {
            tag = getTokenBody(token, getMD5Secret).getExpiration().before(new Date(System.currentTimeMillis()));
        }catch (ExpiredJwtException ex){
            logger.warn(ex.getMessage());
        }catch (SignatureException ex){
            logger.warn(ex.getMessage());
        }
        return tag;
    }

    /**
     * 解密得到 载荷部分
     * @param token
     * @return
     */
    private static Claims getTokenBody(String token,String secret){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}
