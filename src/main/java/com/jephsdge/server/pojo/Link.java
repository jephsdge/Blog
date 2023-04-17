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
@TableName("t_link")
@ApiModel(value = "Link对象", description = "")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("链接ID")
    @TableId(value = "link_id", type = IdType.AUTO)
    private Integer linkId;

    @ApiModelProperty("链接类型")
    @TableField("link_type")
    private Integer linkType;

    @ApiModelProperty("链接名")
    @TableField("link_name")
    private String linkName;

    @ApiModelProperty("链接内容")
    @TableField("link_body")
    private String linkBody;

    @ApiModelProperty("链接描述")
    @TableField("link_description")
    private String linkDescription;

    @ApiModelProperty("是否删除 0-否 1-是")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
