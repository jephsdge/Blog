package com.jephsdge.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jephsdge.server.mapper.CategoryMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogTable {
    private Long blogId;
    private String blogTitle;
    private String blogImg;
    private Integer blogCategoryId;
    private String blogCategoryName;
    private Boolean blogStatus;
    private Long blogViews;
    private LocalDateTime updateTime;

}
