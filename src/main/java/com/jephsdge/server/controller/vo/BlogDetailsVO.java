package com.jephsdge.server.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class BlogDetailsVO {

    private Long blogId;
    private String blogTitle;
    private Integer blogCategoryId;
    private Long commentCount;
    private String blogCategoryIcon;
    private String blogCategoryName;
    private String blogCoverImage;
    private Long blogViews;
    private List<String> blogTags;
    private String blogContent;
    private boolean enableComment;
    private LocalDateTime updateTime;
}
