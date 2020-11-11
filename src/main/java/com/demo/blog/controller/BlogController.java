package com.demo.blog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.blog.common.Result;
import com.demo.blog.entity.Blog;
import com.demo.blog.service.IBlogService;
import com.demo.blog.shiro.ProfileUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xjx
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    //每页的博客数
    private final static Integer pageSize=5;
    @Autowired
    IBlogService blogService;


    //分页查询博客数据
    @RequestMapping("/list/{curPage}")
    public Result listByPage(@PathVariable(value = "curPage")Integer curPage){
        Page<Blog> pageData=new Page<>(curPage,pageSize);
        IPage<Blog> result = blogService.page(pageData, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success("分页查询成功",result);
    }

    //根据博客ID查询某一篇博客
    @RequestMapping("/detail/{id}")
    public Result getDetail(@PathVariable(value = "id")Integer id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"当前博客不存在");
        return Result.success("查询成功",blog);
    }


    //新增或者更新博客
    @RequiresAuthentication
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        ProfileUser user = (ProfileUser) SecurityUtils.getSubject().getPrincipal();
        Blog temp = null;
        if (blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            Assert.isTrue(temp.getUserId().equals(user.getId()), "没有权限编辑");
        } else {
            temp = new Blog();
            temp.setUserId(user.getId());
            temp.setCreated(new Date());
            temp.setStatus(false);
        }
        BeanUtils.copyProperties(blog, temp, "id", "userId", "created");
        blogService.saveOrUpdate(temp);
        return Result.success("编辑成功", null);
    }
}
