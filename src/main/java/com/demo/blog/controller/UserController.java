package com.demo.blog.controller;


import com.demo.blog.common.Result;
import com.demo.blog.entity.User;
import com.demo.blog.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xjx
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;

    @RequiresAuthentication
    @RequestMapping("/index/{id}")
    public Result test(@PathVariable(value = "id")Integer id){
        User byId = userService.getById(id);
        return Result.success("查询成功",byId);
    }

    @RequestMapping("/index/add")
    public Result testInsert(@Validated  @RequestBody User user){
        return Result.success("插入成功",user);
    }
}
