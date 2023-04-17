package com.jephsdge.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jephsdge.server.pojo.Comment;
import com.jephsdge.server.pojo.JqGridData;
import com.jephsdge.server.pojo.JqGridParams;
import com.jephsdge.server.pojo.Message;
import com.jephsdge.server.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Api(tags = "评论")
@Controller
@RequestMapping("/admin")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @ApiOperation("")
    @GetMapping("/comments")
    private String comments(HttpServletRequest request){
        request.setAttribute("path", "comments");
        return "admin/comment";
    }

    @ApiOperation("")
    @GetMapping("/comments/list")
    @ResponseBody
    //_search=false&nd=1681069863584&limit=10&page=1&sidx=&order=asc&totalrows=
    private Message commentsList(JqGridParams params){
        if (ObjectUtils.isEmpty(params.getPage()) || ObjectUtils.isEmpty(params.getLimit())) {
            return Message.error("参数异常");
        }
        Page<Comment> page = params.generatePage();
        Page<Comment> commentPage = commentService.getCommentsByPage(page);
        if (commentPage != null){
            JqGridData jqGridData = JqGridData.generateJqGridData(commentPage);
            return Message.success("查询成功",jqGridData);
        }
        return Message.error("查询失败");
    }

    @ApiOperation("")
    @PostMapping("/comments/checkDone")
    @ResponseBody
    private Message checkDone(@RequestBody Long[] ids){
        if (ids.length < 1) {
            return Message.error("参数异常");
        }
        if (commentService.checkBatch(ids)){
            return Message.success("审核成功");
        }
        return Message.success("审核失败");
    }

    @ApiOperation("")
    @PostMapping("/comments/reply")
    @ResponseBody
    private Message reply(@RequestParam("commentId") Long commentId, @RequestParam("replyBody") String replyBody){
        if (commentId == null || replyBody == null) {
            return Message.error("评论失败");
        }
        if (commentService.reply(commentId, replyBody)){
            return Message.success("评论成功");
        }
        return Message.error("失败");
    }

    @ApiOperation("")
    @PostMapping("/comments/delete")
    @ResponseBody
    private Message delete(@RequestBody Long[] ids){
        if (ids.length < 1){
            return Message.error("参数异常");
        }
        if (commentService.deleteBatch(ids)){
            return Message.success("删除成功");
        }
        return Message.error("删除失败");
    }


}
