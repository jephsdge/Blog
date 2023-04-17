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
@TableName("t_tag")
@ApiModel(value = "Tag对象", description = "")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签ID")
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;

    @ApiModelProperty("标签名")
    @TableField("tag_name")
    private String tagName;

    @ApiModelProperty("删除标志 0-否 1-是")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
