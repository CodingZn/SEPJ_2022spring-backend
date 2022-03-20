package com.example.demo.bean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static final String SING = "s838jjff";

    public  static  String getToken(Map<String, String> map){
        JWTCreator.Builder builder = JWT.create();
//加入默认payload
        Calendar instance = Calendar.getInstance();
        builder.withNotBefore(instance.getTime());//生效时间
        instance.add(Calendar.MINUTE, 10);
        builder.withExpiresAt(instance.getTime());//过期时间

        map.forEach((k, v)->{
            builder.withClaim(k, v);
        });

        String token = builder.sign(Algorithm.HMAC256(SING));

        return token;
    }

    /**
     * 验证token  合法性
     */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
    /**
     * 获取token信息方法
     */
    /*public static DecodedJWT getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }*/


}
