package com.example.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static final String SING = "s838jjff";

    public static final String ValidJWTToken = "Valid";
    public static final String InvalidJWTToken = "Invalid";
    public static final String IncorrectJWTToken = "WrongToken";
    public static final String AdminAuthority = "IsAdmin";
    public static final String TeacherAuthority = "IsTeacher";
    public static final String StudentAuthority = "IsStudent";
    public static final String NoAuthority = "NoAuthority";

    public  static  String generateToken(Map<String, String> map){//生成令牌
        JWTCreator.Builder builder = JWT.create();
//加入默认payload
        Calendar instance = Calendar.getInstance();
        builder.withNotBefore(instance.getTime());//生效时间
        instance.add(Calendar.MINUTE, 300000);//有效期，调试时设为300000，正规发布设为30
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
    private static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    public static String decodeToGetValue(String token, String key){//解码令牌
        String value;
        try{
            DecodedJWT verify = JWTUtils.verify(token);

            value = verify.getClaim(key).asString();
            return value;

        }catch (Exception e) {//无效token

            return null;
        }
    }


}
