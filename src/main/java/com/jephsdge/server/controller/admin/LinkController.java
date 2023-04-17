package com.jephsdge.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jephsdge.server.pojo.*;
import com.jephsdge.server.service.ILinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nonapi.io.github.classgraph.json.Id;
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
@Api(tags = "链接管理模块")
@RequestMapping("/admin")
public class LinkController {
    @Autowired
    private ILinkService linkService;

    @ApiOperation("")
    @GetMapping("/links")
    public String links(HttpServletRequest request){
        request.setAttribute("path","links");
        return "admin/link";
    }

    @ApiOperation("")
    @GetMapping("/links/list")
    @ResponseBody
    //_search=false&nd=1681108913755&limit=10&page=1&sidx=&order=asc&totalrows=
    public Message list(JqGridParams params){
        if (ObjectUtils.isEmpty(params.getPage()) || ObjectUtils.isEmpty(params.getLimit())) {
            return Message.error("参数异常");
        }
        Page<Link> page = params.generatePage();
        Page<Link> linkPage = linkService.getLinkByPage(page);
        if (linkPage != null){
            JqGridData jqGridData = JqGridData.generateJqGridData(linkPage);
            return Message.success("查询成功",jqGridData);
        }
        return Message.error("查询失败");
    }

    @ApiOperation("")
    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Message getInfo(@PathVariable("id") Integer id){
        Link link = linkService.getLinkById(id);
        if (link != null){
            return Message.success("查询成功",link);
        }
        return Message.error("查询失败");
    }

    @ApiOperation("")
    @PostMapping("/links/save")
    @ResponseBody
    private Message save(Link link){
        if (link == null){
            return Message.error("参数错误");
        }
        if (link.getLinkId() == null){
            if (linkService.insert(link)){
                return Message.success("新增成功");
            }
        }else {
            if (linkService.updateById(link)){
                return Message.success("修改成功");
            }
        }
        return Message.error("错误");
    }

    @ApiOperation("")
    @PostMapping("/links/delete")
    @ResponseBody
    public Message delete(@RequestBody Integer[] ids){
        if (ids.length < 1){
            return Message.error("参数错误");
        }
        if (linkService.deleteBatch(ids)){
            return Message.success("删除成功");
        }
        return Message.error("删除失败");
    }

}
