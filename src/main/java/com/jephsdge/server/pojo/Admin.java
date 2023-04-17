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
@TableName("t_admin")
@ApiModel(value = "Admin对象", description = "")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("管理员id")
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;

    @ApiModelProperty("管理员登陆名称")
    @TableField("login_name")
    private String loginName;

    @ApiModelProperty("管理员登陆密码")
    @TableField("login_password")
    private String loginPassword;

    @ApiModelProperty("管理员显示昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("是否锁定 0未锁定 1已锁定无法登陆")
    @TableField("locked")
    private Boolean locked;

    @ApiModelProperty("是否被删除，0表示没有被删除，1表示被删除")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("删除时的日期")
    @TableField("delete_time")
    private LocalDateTime deleteTime;
}
