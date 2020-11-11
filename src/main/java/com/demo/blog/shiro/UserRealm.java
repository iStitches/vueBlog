package com.demo.blog.shiro;

import com.demo.blog.entity.User;
import com.demo.blog.service.IUserService;
import com.demo.blog.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    IUserService userService;

    //为了让realm支持jwt的校验
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JWTToken token=(JWTToken)authenticationToken;
        log.info("jwt-----------{}",token);

        String userId =jwtUtils.getClaimByToken((String) token.getPrincipal()).getSubject();
        User user = userService.getById(userId);

        if(user==null)
            throw new UnknownAccountException("用户不存在");
        if(user.getStatus()==-1)
            throw new LockedAccountException("账号被锁定");
        ProfileUser profileUser = new ProfileUser();
        BeanUtils.copyProperties(user,profileUser);
        log.info("profile---------{}",profileUser.toString());
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(profileUser, token.getCredentials(), this.getName());
        return simpleAuthenticationInfo;
    }
}
