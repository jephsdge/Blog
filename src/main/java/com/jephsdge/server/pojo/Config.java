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
@TableName("t_config")
@ApiModel(value = "Config对象", description = "")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置ID")
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    @ApiModelProperty("配置名")
    @TableField("config_name")
    private String configName;

    @ApiModelProperty("配置项")
    @TableField("config_value")
    private String configValue;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
