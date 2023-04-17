package com.jephsdge.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jephsdge.server.config.Constants;
import com.jephsdge.server.pojo.*;
import com.jephsdge.server.service.IBlogService;
import com.jephsdge.server.service.ICategoryService;
import com.jephsdge.server.utils.BlogTableGenerator;
import com.jephsdge.server.pojo.JqGridParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Api(tags = "后台博客管理")
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private IBlogService blogService;
    @Autowired
    private BlogTableGenerator blogTableGenerator;
    @Autowired
    private ICategoryService categoryService;


    @ApiOperation("跳转到博客管理页")
    @GetMapping("/blogs")
    public String blogs(HttpServletRequest request){
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    @ApiOperation("分页查询获取博客列表")
    @GetMapping("/blogs/list")
    @ResponseBody
    //_search=false&nd=1680907056644&limit=10&page=1&sidx=blogTitle&order=asc&totalrows=&keyword=11
    public Message list(JqGridParams params,HttpServletRequest request){
        if (ObjectUtils.isEmpty(params.getPage()) || ObjectUtils.isEmpty(params.getLimit())) {
            return Message.error("参数异常");
        }
        Page<Blog> page = params.generatePage();
        Page<Blog> blogPage;
        if (null != params.getKeyword()){
            blogPage = blogService.searchAllBlog(page, params.getKeyword());
        }else {
            blogPage =  blogService.getAllBlogsByPage(page);
        }
        if (blogPage != null){
            JqGridData jqGridData = JqGridData.generateJqGridData(blogPage);
            return Message.success("查询成功",jqGridData);
        }

        return Message.error("查询失败");
    }

    @ApiOperation("跳转到博客添加页")
    @GetMapping("/blogs/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    @ApiOperation("根据博客ID跳转到修改页")
    @GetMapping("/blogs/edit/{blogId}")
    public String edit(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
        request.setAttribute("path", "edit");
        Blog blog = blogService.getBlogById(blogId);
        if (blog == null) {
            return "error/error_400";
        }
        request.setAttribute("blog", blog);
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    @ApiOperation("存储博客请求")
    @PostMapping("/blogs/save")
    @ResponseBody
    public Message save(Blog blog) {
//        int i = blog.getBlogImg().indexOf("/upload/");
//        String substring = blog.getBlogImg().substring(i);
//        blog.setBlogImg(substring);
        blog.setBlogCategoryName(categoryService.getCategoryById(blog.getBlogCategoryId()).getCategoryName());
        if (blog.getBlogId() == null){
            if (blogService.addBlog(blog)){
                return Message.success("添加成功");
            }
        }else {
            if (blogService.updateBlog(blog)){
                return Message.success("修改成功");
            }
        }
        return Message.error("操作失败");
    }


    @ApiOperation("删除请求，逻辑删除")
    @PostMapping("/blogs/delete")
    @ResponseBody
    public Message delete(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return Message.error("参数异常！");
        }
        if (blogService.deleteBatch(ids)) {
            return Message.success("删除成功");
        } else {
            return Message.error("删除失败");
        }
    }

}
