package com.demo.blog.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProfileUser implements Serializable {
    private Integer id;
    private String username;
    private String avatar;
}
