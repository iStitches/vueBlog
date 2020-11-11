package com.demo.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

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
@TableName("m_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 博主ID
     */
    @JsonProperty(value = "user_id")
    private Integer userId;

    /**
     * 博客标题
     */
    @NotBlank(message = "文章标题不能为空！")
    private String title;

    /**
     * 博客描述
     */
    @NotBlank(message = "文章描述不能为空")
    private String description;

    /**
     * 博客内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 博客创建时间
     */
    private Date created;

    /**
     * 博客发布状态
     */
    private Boolean status;


}
