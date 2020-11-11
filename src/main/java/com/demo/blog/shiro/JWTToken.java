package com.demo.blog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义JWTToken
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public JWTToken(String token){
        this.token=token;
    }
}
