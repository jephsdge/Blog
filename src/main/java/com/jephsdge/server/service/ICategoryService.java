package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.pojo.Category;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
public interface ICategoryService extends IService<Category> {

    long getCategorysCount();

    List<Category> getAllCategories();

    Category getCategoryById(Integer categoryId);

    Page<Category> getCategoryByPage(Page<Category> page);

    boolean insert(Category category);

    boolean deleteCategoryById(Integer id);

    boolean deleteBatch(Integer[] ids);

    List<Category> searchCategory(String search);
}
