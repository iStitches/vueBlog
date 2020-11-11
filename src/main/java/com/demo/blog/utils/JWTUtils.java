package com.demo.blog.utils;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT的工具类：生成JWT、校验JWT是否过期、获取jwt的信息
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "mytest.jwt")
public class JWTUtils {
    private Long expire;
    private String header;
    private String secret;

    //生成jwtToken
    public String generateToken(String userId){
        Date date=new Date();
        Date expireTime=new Date(date.getTime()+expire*1000);

        return Jwts.builder()
                .setHeaderParam("jwt",header)
                .setSubject(userId)
                .setIssuedAt(date)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    //通过token获取jwt的信息---claim
    public Claims getClaimByToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error",e);
            return null;
        }
    }

    //检查jwt是否过期
    public boolean isJwtExpire(Date expireTime){
        return expireTime.before(new Date());
    }
}
