package com.example.recommendation.utils;

import com.example.recommendation.entity.User;
import com.example.recommendation.service.UserService;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProccessor {
    private static UserService userService;
    private static final long EXPIRE_TIME= 60 * 60 * 1000 * 24; //过期时间1天
    private static final String KEY = "huterox"; //加密秘钥

    public static String createToken(String username){
        Map<String,Object> header = new HashMap();
        header.put("typ","JWT");
        header.put("alg","HS256");
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .setSubject(username)//设置信息，也就是用户名
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,KEY);//加密方式
        return builder.compact();
    }
    public static int verify(String token){
        Claims claims = null;
        try {
            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
            claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return 2;
        }
        //从token中获取用户名，当用户查询通过后即可
        String account = claims.getSubject();
        User user = userService.selectUserByAccount(account);
        if(user != null){
            return 1;
        }else{
            return 0;
        }
    }
}
