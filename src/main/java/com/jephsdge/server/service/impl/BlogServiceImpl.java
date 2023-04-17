package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.controller.vo.BlogDetailsVO;
import com.jephsdge.server.controller.vo.BlogDetailsVOGenerator;
import com.jephsdge.server.mapper.BlogAndTagMapper;
import com.jephsdge.server.mapper.BlogMapper;
import com.jephsdge.server.mapper.TagMapper;
import com.jephsdge.server.pojo.*;
import com.jephsdge.server.service.IBlogService;
import com.jephsdge.server.service.ICategoryService;
import com.jephsdge.server.service.ICommentService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogAndTagMapper blogAndTagMapper;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private BlogDetailsVOGenerator blogDetailsVOGenerator;
    @Override
    public long getBlogsCount() {

        return blogMapper.selectCount(
                new QueryWrapper<Blog>()
                        .eq("is_delete",false)
        );
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogMapper.selectList(null);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public boolean addBlog(Blog blog) {
        if (blog.getBlogId()==null){
            long now = System.currentTimeMillis();
            Random r = new Random();
            now += r.nextInt(100);
            blog.setBlogId(now);
        }
        String blogTag = blog.getBlogTag();
        String[] tags = blogTag.split(",");
        boolean flag = true;

        if (blog.getBlogStatus()){
            Integer categoryId = blog.getBlogCategoryId();
            Category category = categoryService.getCategoryById(categoryId);
            category.setCategoryNums(category.getCategoryNums()+1);
            flag = categoryService.updateById(category);
        }


        flag &= blogMapper.insert(blog) == 1;
        if (flag){
            for (String tagName:tags){
                Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>()
                        .eq("tag_name", tagName));
                if (tag == null){
                    tag = new Tag();
                    long now = System.currentTimeMillis();
                    Random r = new Random();
                    now += r.nextInt(100);
                    tag.setTagId(now);
                    tag.setTagName(tagName);
                    flag &= (tagMapper.insert(tag)==1);
                    if (flag){
                        BlogAndTag blogAndTag = new BlogAndTag();
                        blogAndTag.setBlogId(blog.getBlogId());
                        blogAndTag.setTagId(tag.getTagId());
                        flag &= blogAndTagMapper.insert(blogAndTag) == 1;
                    }
                }
                else {
                    BlogAndTag blogAndTag = new BlogAndTag();
                    blogAndTag.setBlogId(blog.getBlogId());
                    blogAndTag.setTagId(tag.getTagId());
                    flag &= blogAndTagMapper.insert(blogAndTag) == 1;
                }
            }
        }
        if (!flag){
            throw new MyRuntimeException("错误");
        }
        return flag;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public boolean updateBlog(Blog blog) {
        String blogTag = blog.getBlogTag();
        String[] tags = blogTag.split(",");
        boolean flag = true;

        Blog oldBlog = blogMapper.selectById(blog.getBlogId());
        if (oldBlog.getBlogStatus()){
            Integer oldCategoryId = oldBlog.getBlogCategoryId();
            Category oldCategory = categoryService.getCategoryById(oldCategoryId);
            oldCategory.setCategoryNums(oldCategory.getCategoryNums()-1);
            flag = categoryService.updateById(oldCategory);
        }
        if (blog.getBlogStatus()){
            Integer newCategoryId = blog.getBlogCategoryId();
            Category newCategory = categoryService.getCategoryById(newCategoryId);
            newCategory.setCategoryNums(newCategory.getCategoryNums()+1);
            flag &= categoryService.updateById(newCategory);
        }

        blog.setUpdateTime(LocalDateTime.now());
        flag &= blogMapper.updateById(blog) == 1;
        if (flag){
            for (String tagName:tags){
                Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>()
                        .eq("tag_name", tagName));
                if (tag == null){
                    tag = new Tag();
                    long now = System.currentTimeMillis();
                    Random r = new Random();
                    now += r.nextInt(100);
                    tag.setTagId(now);
                    tag.setTagName(tagName);
                    flag &= (tagMapper.insert(tag)==1);
                    if (flag){
                        BlogAndTag blogAndTag = new BlogAndTag();
                        blogAndTag.setBlogId(blog.getBlogId());
                        blogAndTag.setTagId(tag.getTagId());
                        flag &= blogAndTagMapper.insert(blogAndTag) == 1;
                    }
                }
                else {
                    BlogAndTag blogAndTag = blogAndTagMapper.selectOne(new QueryWrapper<BlogAndTag>()
                            .eq("blog_id", blog.getBlogId())
                            .eq("tag_id", tag.getTagId()));
                    if (blogAndTag==null){
                        blogAndTag = new BlogAndTag();
                        blogAndTag.setBlogId(blog.getBlogId());
                        blogAndTag.setTagId(tag.getTagId());
                        flag &= blogAndTagMapper.insert(blogAndTag) == 1;
                    }
                }
            }
        }
        if (!flag){
            throw new MyRuntimeException("错误");
        }
        return flag;
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogMapper.selectById(blogId);
    }

    @Override
    public List<Blog> getBlogByCategoryId(Integer categoryId) {
        return blogMapper.selectList(new QueryWrapper<Blog>()
                .eq("blog_category_id",categoryId)
                .eq("is_delete",false));
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        boolean flag = true;
        for (Long id : ids){
            flag &= deleteByBlogId(id);
        }
        return flag;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public boolean deleteByBlogId(Long id) {
        boolean flag = true;

        Blog blog = blogMapper.selectById(id);
        blog.setIsDelete(true);
        blog.setDeleteTime(LocalDateTime.now());

        Integer categoryId = blog.getBlogCategoryId();
        Category category = categoryService.getCategoryById(categoryId);
        category.setCategoryNums(category.getCategoryNums()-1);
        flag = categoryService.updateById(category);

        flag &= commentService.deleteCommentByBlogId(blog.getBlogId());
        if (flag){
            flag = blogMapper.updateById(blog) == 1;
        }
        if (!flag){
            throw new MyRuntimeException("删除博客ID为"+id+"时出错");
        }
        return flag;
    }


    @Override
    public Page<Blog> getAllBlogsByPage(Page<Blog> page) {
        Page<Blog> blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                .eq("is_delete", false));
        List<Blog> records = blogPage.getRecords();
        for (Blog blog:records){
            Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
            blog.setBlogCategoryName(category.getCategoryName());
        }
        blogPage.setRecords(records);
        return blogPage;
    }

    @Override
    public Page<Blog> getBlogsByPage(Page<Blog> page) {
        Page<Blog> blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                .eq("blog_status",true)
                .eq("is_delete", false));
        List<Blog> records = blogPage.getRecords();
        for (Blog blog:records){
            Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
            blog.setBlogCategoryName(category.getCategoryName());
        }
        blogPage.setRecords(records);
        return blogPage;
    }

    @Override
    public Page<Blog> searchAllBlog(Page<Blog> page, String search){
        Page<Blog> blogPage;
        List<Category> categoryList = categoryService.searchCategory(search);
        if (categoryList.isEmpty()){
            blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                    .eq("is_delete", false)
                    .like("blog_title", search)
                    );
            List<Blog> records = blogPage.getRecords();
            for (Blog blog:records){
                Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
                blog.setBlogCategoryName(category.getCategoryName());
            }
            blogPage.setRecords(records);
        }else {
            List<Integer> categoryIds = new ArrayList<>();
            for (Category category:categoryList){
                categoryIds.add(category.getCategoryId());
            }
            blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                    .eq("is_delete", false)
                    .and(i -> i.like("blog_title", search)
                            .or()
                            .in("blog_category_id", categoryIds)
                    ));
            List<Blog> records = blogPage.getRecords();
            for (Blog blog:records){
                Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
                blog.setBlogCategoryName(category.getCategoryName());
            }
            blogPage.setRecords(records);
        }
        return blogPage;
    }

    /**
     * 考虑博客是否发布
     * @param page
     * @param search
     * @return
     */
    @Override
    public Page<Blog> searchBlog(Page<Blog> page, String search){
        Page<Blog> blogPage;
        List<Category> categoryList = categoryService.searchCategory(search);
        if (categoryList.isEmpty()){
            blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                    .eq("is_delete", false).eq("blog_status",true)
                    .like("blog_title", search)
            );
            List<Blog> records = blogPage.getRecords();
            for (Blog blog:records){
                Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
                blog.setBlogCategoryName(category.getCategoryName());
            }
            blogPage.setRecords(records);
        }else {
            List<Integer> categoryIds = new ArrayList<>();
            for (Category category:categoryList){
                categoryIds.add(category.getCategoryId());
            }
            blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                    .eq("is_delete", false)
                    .eq("blog_status",true)
                    .and(i -> i.like("blog_title", search)
                            .or()
                            .in("blog_category_id", categoryIds)
                    ));
            List<Blog> records = blogPage.getRecords();
            for (Blog blog:records){
                Category category = categoryService.getCategoryById(blog.getBlogCategoryId());
                blog.setBlogCategoryName(category.getCategoryName());
            }
            blogPage.setRecords(records);
        }
        return blogPage;
    }

    @Override
    public Page<Blog> getBlogsByCategory(Page<Blog> page, Integer categoryId) {
        return blogMapper.selectPage(page,new QueryWrapper<Blog>()
                .eq("blog_category_id",categoryId)
                .eq("blog_status",true)
                .eq("is_delete",false));
    }

    @Override
    public List<Blog> getNewBlogs(int i) {
        Page<Blog> page = new Page<>();
        page.setCurrent(1);
        page.setSize(i);
        page.addOrder(OrderItem.desc("updateTime"));
        Page<Blog> blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                .eq("blog_status",true)
                .eq("is_delete", false));

        return blogPage.getRecords();
    }

    @Override
    public List<Blog> getHotBlogs(int i) {
        Page<Blog> page = new Page<>();
        page.setCurrent(1);
        page.setSize(i);
        page.addOrder(OrderItem.desc("blogViews"));
        Page<Blog> blogPage = blogMapper.selectPage(page, new QueryWrapper<Blog>()
                .eq("blog_status",true)
                .eq("is_delete", false));
        return blogPage.getRecords();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public BlogDetailsVO getBlogVOByID(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        boolean flag = true;
        if (blog == null){
            return null;
        }
        blog.setBlogViews(blog.getBlogViews()+1);
        flag = blogMapper.updateById(blog) == 1;
        if (!flag){
            throw new MyRuntimeException("点击错误");
        }
        return blogDetailsVOGenerator.generateBlogDetailsVO(blog);
    }
}
