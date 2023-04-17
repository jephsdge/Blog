package com.jephsdge.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jephsdge.server.pojo.Category;
import com.jephsdge.server.pojo.JqGridData;
import com.jephsdge.server.pojo.JqGridParams;
import com.jephsdge.server.pojo.Message;
import com.jephsdge.server.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Controller
@Api(tags = "分类模块")
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @ApiOperation("")
    @GetMapping("/categories")
    private String category(HttpServletRequest request){
        request.setAttribute("path","categories");
        return "admin/category";
    }


    @ApiOperation("")
    @GetMapping("/category/getNameById")
    @ResponseBody
    public Message getNameById(@RequestParam("categoryId") Integer categoryId){
        if (null == categoryId){
            return Message.error("未分类");
        }
        Category category = categoryService.getCategoryById(categoryId);
        if (null != category){
            return Message.success(category.getCategoryName());
        }
        return Message.error("错误");
    }

    @ApiOperation("")
    @GetMapping("/categories/list")
    @ResponseBody
    //_search=false&nd=1681097774902&limit=10&page=1&sidx=&order=asc&totalrows=
    private Message list(JqGridParams params){
        if (ObjectUtils.isEmpty(params.getPage()) || ObjectUtils.isEmpty(params.getLimit())) {
            return Message.error("参数异常");
        }
        Page<Category> page = params.generatePage();
        Page<Category> categoryPage = categoryService.getCategoryByPage(page);
        if (categoryPage != null){
            JqGridData jqGridData = JqGridData.generateJqGridData(categoryPage);
            return Message.success("查询成功",jqGridData);
        }
        return Message.error("查询失败");
    }

    @ApiOperation("")
    @PostMapping("/categories/save")
    @ResponseBody
    private Message save(Category category){
        if (category.getCategoryName() == null || category.getCategoryIcon() == null){
            return Message.error("参数异常");
        }
        if (category.getCategoryId() == null){
            if (categoryService.insert(category)){
                return Message.success("新增成功");
            }
        }else {
            if (categoryService.updateById(category)){
                return Message.success("修改成功");
            }
        }
        return Message.error("失败");
    }

    @ApiOperation("")
    @PostMapping("/categories/delete")
    @ResponseBody
    private Message delete(@RequestBody Integer[] ids){
        if (ids.length < 1){
            return Message.error("参数错误");
        }
        if (categoryService.deleteBatch(ids)){
            return Message.success("删除成功");
        }
        return Message.error("删除失败");
    }

}
