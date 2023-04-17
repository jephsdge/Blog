package com.jephsdge.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_blog_tag")
@ApiModel(value = "blog和tag的关系", description = "")
public class BlogAndTag {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("博客标签ID")
    @TableId(value = "blog_tag_id", type = IdType.AUTO)
    private Long blogTagId;

    @ApiModelProperty("博客ID")
    @TableField("blog_id")
    private Long blogId;

    @ApiModelProperty("标签ID")
    @TableField("tag_id")
    private Long tagId;
}
