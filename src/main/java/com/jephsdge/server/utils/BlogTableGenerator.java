package com.jephsdge.server.utils;

import com.jephsdge.server.mapper.CategoryMapper;
import com.jephsdge.server.pojo.Blog;
import com.jephsdge.server.pojo.BlogTable;
import com.jephsdge.server.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlogTableGenerator {
    @Autowired
    private CategoryMapper categoryMapper;

    public BlogTable generateFromBlog(Blog blog){
        Category category = categoryMapper.selectById(blog.getBlogCategoryId());
        return new BlogTable(
                blog.getBlogId(),
                blog.getBlogTitle(),
                blog.getBlogImg(),
                blog.getBlogCategoryId(),
                category.getCategoryName(),
                blog.getBlogStatus(),
                blog.getBlogViews(),
                blog.getUpdateTime());
    }
    public List<BlogTable> generateFromBlogList(List<Blog> blogList){
        List<BlogTable> blogTableList = new ArrayList<>();
        for (Blog blog:blogList){
            blogTableList.add(generateFromBlog(blog));
        }
        return blogTableList;
    }
}
