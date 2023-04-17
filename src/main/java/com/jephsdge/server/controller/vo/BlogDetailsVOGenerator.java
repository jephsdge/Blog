package com.jephsdge.server.controller.vo;

import com.jephsdge.server.pojo.Blog;
import com.jephsdge.server.pojo.Category;
import com.jephsdge.server.service.ICategoryService;
import com.jephsdge.server.service.ICommentService;
import com.jephsdge.server.utils.MarkDownUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class BlogDetailsVOGenerator {

    @Autowired
    private ICommentService commentService;
    @Autowired
    private ICategoryService categoryService;

    public BlogDetailsVO generateBlogDetailsVO(Blog blog){
        BlogDetailsVO blogDetailsVO = new BlogDetailsVO();
        blogDetailsVO.setBlogId(blog.getBlogId());
        blogDetailsVO.setBlogTitle(blog.getBlogTitle());
        blogDetailsVO.setBlogCategoryId(blog.getBlogCategoryId());
        blogDetailsVO.setCommentCount(commentService.getCommetnCountByBlogId(blog.getBlogId()));
        Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
        blogDetailsVO.setBlogCategoryIcon(category.getCategoryIcon());
        blogDetailsVO.setBlogCategoryName(category.getCategoryName());
        blogDetailsVO.setBlogCoverImage(blog.getBlogImg());
        blogDetailsVO.setBlogViews(blog.getBlogViews());
        String blogTag = blog.getBlogTag();
        String[] split = blogTag.split(",");
        List<String> blogTags = Arrays.asList(split);
        blogDetailsVO.setBlogTags(blogTags);
        blogDetailsVO.setBlogContent(MarkDownUtil.mdToHtml(blog.getBlogContent()));
        blogDetailsVO.setEnableComment(blog.getEnableComment());
        blogDetailsVO.setUpdateTime(blog.getUpdateTime());
        return blogDetailsVO;
    }
}
