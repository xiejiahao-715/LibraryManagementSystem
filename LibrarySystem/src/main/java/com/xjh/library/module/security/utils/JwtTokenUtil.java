package com.xjh.library.module.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    // 公用密钥-保存在服务端,客户端是不会知道密钥的,以防被攻击
    private static final String TOKEN_SECRET = "5xcJVrXNy*&%$^@#&@^!&@&#@&";

    // token的过期时间  默认为7天
    public static final Duration TOKEN_EXPIRE_TIME = Duration.ofDays(7);

    // token在请求头中的存储的名字
    public static final String TOKEN_HEADER_NAME = "token";

    /**
     * 生成token
     * @param uid   管理员的唯一标识 uid
     * @return 返回token字符串
     */
    public static String buildToken(Long uid){
        if(uid == null) return null;
        Map<String,Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // 当前的时间
        long currentTimeMillis = System.currentTimeMillis();
        // 生成签发时间
        Date issuedAtTime = new Date(currentTimeMillis);
        // 生成过期时间
        Date expiresAtTime = new Date(currentTimeMillis+TOKEN_EXPIRE_TIME.toMillis());
        return JWT.create()
                .withHeader(map)
                .withIssuer("xjh")  // 发布者
                .withSubject("library-system")  // 主题
                .withIssuedAt(issuedAtTime)  // 生成时间
                .withExpiresAt(expiresAtTime)  // 过期时间
                .withClaim("uid",uid)  //自定义字段存数据
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }
    /**
     * 校验token，正确就可以得到用户id
     * @param token 需要校验的token
     * @return 如果token正确则可以获取到存储在token中的uid，并返回uid，否则token无效
     */
    public static Long verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("uid").asLong();
        }catch (Exception e){
            return null;
        }
    }
}
