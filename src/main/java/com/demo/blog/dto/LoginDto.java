package com.demo.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginDto {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Pattern(regexp = "^[a-z A-Z 0-9]{6,}",message = "密码必须为字母或者数字的6位组合")
    private String password;
}
