package com.demo.blog.filter;


import cn.hutool.json.JSONUtil;
import com.demo.blog.common.Result;
import com.demo.blog.shiro.JWTToken;
import com.demo.blog.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前后端分离
 * 自定义JWTFilter拦截器，拦截一切请求
 */
@Component
public class JWTFilter extends AuthenticatingFilter {
    @Autowired
    JWTUtils jwtUtils;

    //自定义token，然后交给shiro验证
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt=request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt))
            return null;
        else
            return new JWTToken(jwt);
    }

    //拦截校验
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");

        //请求头没有jwt，身份为游客，直接放行
        if(StringUtils.isEmpty(jwt))
            return true;
        //否则用户需要进行登录验证操作
        else{
            //验证jwt有效
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if(claim==null || jwtUtils.isJwtExpire(claim.getExpiration()))
                throw new ExpiredCredentialsException("token已经过期，请重新登录");
        }
        //shiro登录验证
        return executeLogin(servletRequest,servletResponse);
    }

    //处理登录失败时的异常
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        try {
            Throwable failure=e.getCause()==null?e:e.getCause();
            Result result=Result.fail(failure.getMessage());
            String json= JSONUtil.toJsonStr(result);
            servletResponse.getWriter().print(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //对跨域提供支持
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest= WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse=WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin",httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-control-Allow-Headers",httpServletRequest.getHeader("Access-Control-Request-Headers"));
        //复杂请求跨域时首先会发送一个预检请求(OPTIONS)，这里直接返回正常状态
        if(httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request,response);
    }
}
