package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.controller.vo.BlogDetailsVO;
import com.jephsdge.server.pojo.Blog;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
public interface IBlogService extends IService<Blog> {

    long getBlogsCount();

    List<Blog> getAllBlogs();

    Page<Blog> getBlogsByPage(Page<Blog> page);

    Page<Blog> getAllBlogsByPage(Page<Blog> page);

    boolean addBlog(Blog blog);

    boolean updateBlog(Blog blog);

    Blog getBlogById(Long blogId);

    boolean deleteBatch(Long[] ids);

    boolean deleteByBlogId(Long id);

    List<Blog> getBlogByCategoryId(Integer categoryId);

    Page<Blog> searchBlog(Page<Blog> page, String search);
    Page<Blog> searchAllBlog(Page<Blog> page, String keyword);

    Page<Blog> getBlogsByCategory(Page<Blog> page, Integer categoryId);

    List<Blog> getNewBlogs(int i);

    List<Blog> getHotBlogs(int i);

    BlogDetailsVO getBlogVOByID(Long blogId);

}
