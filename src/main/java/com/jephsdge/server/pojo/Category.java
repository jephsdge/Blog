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
@TableName("t_category")
@ApiModel(value = "Category对象", description = "")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类ID")
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @ApiModelProperty("分类名")
    @TableField("category_name")
    private String categoryName;

    @ApiModelProperty("分类图标")
    @TableField("category_icon")
    private String categoryIcon;

    @ApiModelProperty("分类数量")
    @TableField("category_nums")
    private Integer categoryNums;

    @ApiModelProperty("删除标志 0-否 1是")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("删除时间")
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
