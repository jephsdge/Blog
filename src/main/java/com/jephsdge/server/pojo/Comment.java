package com.jephsdge.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@TableName(value = "t_comment",excludeProperty = {"blogTitle"})
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty("博客ID")
    @TableField("blog_id")
    private Long blogId;

    private String blogTitle;

    @ApiModelProperty("评论者名")
    @TableField("commentator_name")
    private String commentatorName;

    @ApiModelProperty("评论者邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("评论内容")
    @TableField("comment_body")
    private String commentBody;

    @ApiModelProperty("评论时间")
    @TableField("comment_time")
    private LocalDateTime commentTime;

    @ApiModelProperty("评论者IP")
    @TableField("commentator_ip")
    private String commentatorIp;

    @ApiModelProperty("回复内容")
    @TableField("reply_body")
    private String replyBody;

    @ApiModelProperty("回复时间")
    @TableField("reply_time")
    private LocalDateTime replyTime;

    @ApiModelProperty("评论状态 0-未审核 1-过审核")
    @TableField("comment_status")
    private Boolean commentStatus;

    @ApiModelProperty("删除标志 0-否 1-是")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private LocalDateTime deleteTime;
}
