package com.demo.blog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.blog.common.Result;
import com.demo.blog.dto.LoginDto;
import com.demo.blog.entity.User;
import com.demo.blog.service.IUserService;
import com.demo.blog.utils.JWTUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {
    @Autowired
    IUserService userService;
    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",loginDto.getUsername());
        //判断用户名和密码
        User one = userService.getOne(wrapper);
        Assert.notNull(one,"用户不存在！");

        if(!one.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码错误！");
        }
        //生成jwt放入响应头，以后每次请求都会携带jwt
        String jwt = jwtUtils.generateToken(one.getId().toString());
        response.setHeader("Authorization",jwt);
        //在涉及跨域请求时,response中大部分header需要源服务端同意才能拿到,所以需要在response中增加一个如下header
        response.setHeader("Access-Control-Expose-Headers","Authorization");
        return Result.success("登录成功", MapUtil.builder()
                                    .put("id",one.getId())
                                    .put("username",one.getUsername())
                                    .put("avatar",one.getAvatar())
                                    .put("email",one.getEmail())
                                    .put("id",one.getId())
                                    .put("id",one.getId()));
    }

    @GetMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.success("注销成功",null);
    }

    @PostMapping("/register")
    public Result register(@Validated @RequestBody User user){
        String pwd=SecureUtil.md5(user.getPassword());
        user.setPassword(pwd);
        userService.save(user);
        return Result.success("插入成功",null);
    }
}
