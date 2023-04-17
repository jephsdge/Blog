package com.jephsdge.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jephsdge.server.pojo.JqGridData;
import com.jephsdge.server.pojo.JqGridParams;
import com.jephsdge.server.pojo.Message;
import com.jephsdge.server.pojo.Tag;
import com.jephsdge.server.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Controller
@Api(tags = "标签管理模块")
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private ITagService tagService;

    @ApiOperation("")
    @GetMapping("/tags")
    public String tag(HttpServletRequest request){
        request.setAttribute("path","tags");
        return "admin/tag";
    }

    @ApiOperation("")
    @GetMapping("/tags/list")
    @ResponseBody
    //search=false&nd=1681107358555&limit=10&page=1&sidx=&order=asc&totalrows=
    public Message list(JqGridParams params){
        if (ObjectUtils.isEmpty(params.getPage()) || ObjectUtils.isEmpty(params.getLimit())) {
            return Message.error("参数异常");
        }
        Page<Tag> page = params.generatePage();
        Page<Tag> tagPage = tagService.getTagsByPage(page);
        if (tagPage != null){
            JqGridData jqGridData = JqGridData.generateJqGridData(tagPage);
            return Message.success("查询成功",jqGridData);
        }
        return Message.error("查询失败");
    }

    @ApiOperation("")
    @PostMapping("/tags/save")
    @ResponseBody
    public Message save(Tag tag){
        if (tag.getTagName() == null){
            return Message.error("参数错误");
        }
        if (tagService.insert(tag)){
            return Message.success("添加成功");
        }
        return Message.error("添加失败");
    }

    @ApiOperation("")
    @PostMapping("/tags/delete")
    @ResponseBody
    public Message delete(@RequestBody Long[] ids){
        if (ids.length < 1){
            return Message.error("参数错误");
        }
        if (tagService.deleteBatch(ids)){
            return Message.success("删除成功");
        }
        return Message.error("删除失败");
    }
}
