package com.jephsdge.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-07
 */
@Data
@TableName(value = "t_blog",excludeProperty = {"blogCategoryName"})
@ApiModel(value = "Blog对象", description = "")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("博客ID")
    @TableId(value = "blog_id", type = IdType.AUTO)
    private Long blogId;

    @ApiModelProperty("博客标题")
    @TableField("blog_title")
    private String blogTitle;

    @ApiModelProperty("博客副标题")
    @TableField("blog_subtitle")
    private String blogSubTitle;

    @ApiModelProperty("博客封面图")
    @TableField("blog_img")
    private String blogImg;

    @ApiModelProperty("博客内容")
    @TableField("blog_content")
    private String blogContent;

    @ApiModelProperty("博客分类ID")
    @TableField("blog_category_id")
    private Integer blogCategoryId;

    @ApiModelProperty("博客分类名")
    private String blogCategoryName;

    @ApiModelProperty("博客标签")
    @TableField("blog_tag")
    private String blogTag;

    @ApiModelProperty("博客状态 0-草稿 1-发布")
    @TableField("blog_status")
    private Boolean blogStatus;

    @ApiModelProperty("阅读量")
    @TableField("blog_views")
    private Long blogViews;

    @ApiModelProperty("评论开关 0-不允许 1-允许")
    @TableField("enable_comment")
    private Boolean enableComment;

    @ApiModelProperty("是否被删除 0-否 1-是")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

}
