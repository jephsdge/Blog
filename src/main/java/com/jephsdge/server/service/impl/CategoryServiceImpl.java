package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.mapper.CategoryMapper;
import com.jephsdge.server.pojo.Blog;
import com.jephsdge.server.pojo.Category;
import com.jephsdge.server.pojo.MyRuntimeException;
import com.jephsdge.server.service.IBlogService;
import com.jephsdge.server.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IBlogService blogService;
    @Override
    public long getCategorysCount() {
        return categoryMapper.selectCount(
            new QueryWrapper<Category>()
                    .eq("is_delete",false)
        );
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(new QueryWrapper<Category>()
                .eq("is_delete",false));
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public Page<Category> getCategoryByPage(Page<Category> page) {
        return categoryMapper.selectPage(page,new QueryWrapper<Category>()
                .eq("is_delete",false));
    }

    @Override
    public boolean insert(Category category) {
        return categoryMapper.insert(category)==1;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public boolean deleteCategoryById(Integer id) {
        boolean flag = true;
        Category category = categoryMapper.selectById(id);
        category.setIsDelete(true);
        category.setDeleteTime(LocalDateTime.now());
        List<Blog> blogList = blogService.getBlogByCategoryId(category.getCategoryId());
        for (Blog blog:blogList){
            flag &= blogService.deleteByBlogId(blog.getBlogId());
        }
        if (flag){
            flag &= categoryMapper.updateById(category) == 1;
        }
        if (!flag){
            throw new MyRuntimeException("删除ID为"+id+"的分组时出错");
        }
        return flag;
    }


    @Override
    public boolean deleteBatch(Integer[] ids) {
        boolean flag = true;
        for (Integer id : ids){
            flag &= deleteCategoryById(id);
        }
        return flag;
    }

    @Override
    public List<Category> searchCategory(String search) {
        return categoryMapper.selectList(new QueryWrapper<Category>()
                .eq("is_delete",false)
                .like("category_name",search));
    }
}
