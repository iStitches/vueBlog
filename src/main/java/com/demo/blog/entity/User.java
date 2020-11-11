package com.demo.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjx
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱不符合规范")
    private String email;

    /**
     * 登录密码
     */
    @Pattern(regexp = "^[a-z A-Z 0-9]{6,}",message = "密码必须为字母或者数字的6位组合")
    private String password;

    /**
     * 账户状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 上一次登录时间
     */
    private Date lastLogin;


}
