package com.jephsdge.server.controller;

import cn.hutool.captcha.ShearCaptcha;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jephsdge.server.controller.vo.BlogDetailsVO;
import com.jephsdge.server.controller.vo.BlogDetailsVOGenerator;
import com.jephsdge.server.pojo.*;
import com.jephsdge.server.service.*;
import com.jephsdge.server.utils.SessionAttributeName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Controller
public class VisitorBlogController {
    @Autowired
    private IBlogService blogService;
    @Autowired
    private IConfigService configService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private ILinkService linkService;

    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request){
        request.setAttribute("pageName","首页");
        return page(request,1);
    }
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }
    @GetMapping({"/category/{categoryId}"})
    public String category(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
        return category(request,categoryId,1);
    }
    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        Page<Blog> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(8);
        Page<Blog> blogsPage = blogService.getBlogsByPage(page);
        if (blogsPage == null){
            return "error/error_404";
        }
        JqGridData jqGridData = JqGridData.generateJqGridData(blogsPage);
        request.setAttribute("categories", categoryService.getAllCategories());
        request.setAttribute("blogsPage",jqGridData);
        request.setAttribute("newBlogs", blogService.getNewBlogs(5));
        request.setAttribute("hotBlogs", blogService.getHotBlogs(5));
        request.setAttribute("configurations",configService.getAllConfigs());
        return "blog/amaze/index";
    }

    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer pageNum) {
        Page<Blog> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(8);
        Page<Blog> blogsPage = blogService.searchBlog(page, keyword);
        return keywordList(request,keyword,"搜索","search",blogsPage);
    }

    @GetMapping({"/category/{categoryId}/{pageNum}"})
    public String category(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("pageNum") Integer pageNum) {
        Page<Blog> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(8);
        Page<Blog> blogsPage = blogService.getBlogsByCategory(page,categoryId);
        return keywordList(request,categoryId+"","分类","category",blogsPage);
    }

    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        Map<Integer, List<Link>> linkMap = linkService.getLinksMap();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey( 0)) {
                request.setAttribute("favoriteLinks", linkMap.get(0));
            }
            if (linkMap.containsKey(1)) {
                request.setAttribute("recommendLinks", linkMap.get(1));
            }
            if (linkMap.containsKey(2)) {
                request.setAttribute("personalLinks", linkMap.get(2));
            }
        }
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/amaze/link";
    }

    @GetMapping("/about")
    public String about(HttpServletRequest request){
        return detail(request, 0L,1);
    }

    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request,
                         @PathVariable("blogId") Long blogId,
                         @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPageNum) {
        BlogDetailsVO blogDetailVO = blogService.getBlogVOByID(blogId);
        if (blogDetailVO != null) {
            Page<Comment> page = new Page<>();
            page.setCurrent(commentPageNum);
            page.setSize(5);
            Page<Comment> commentPage =  commentService.getCommentPageByBlogIdAndPage(blogId, page);
            JqGridData jqGridData = JqGridData.generateJqGridData(commentPage);
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", jqGridData);
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/amaze/detail";
    }


    @PostMapping("/blog/comment")
    @ResponseBody
    public Message comment(
            HttpSession session,
            @RequestParam("blogId") Long blogId,
            @RequestParam("verifyCode") String verifyCode,
            @RequestParam("commentatorName") String commentatorName,
            @RequestParam("email") String email,
            @RequestParam("commentatorIp") String commentatorIp,
            @RequestParam("commentBody") String commentBody
    ){
        Comment comment = new Comment();
        comment.setBlogId(blogId);
        comment.setCommentatorName(commentatorName);
        comment.setEmail(email);
        comment.setCommentatorIp(commentatorIp);
        comment.setCommentBody(commentBody);
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute(SessionAttributeName.VERIFYCODE);
        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)){
            return Message.error("验证码错误");
        }
        if(commentService.addComment(comment)){
            return Message.success("评论成功");
        }
        return Message.error("评论失败");
    }

    private String keywordList(HttpServletRequest request,String keyword,String pageName,String pageUrl, Page<Blog> page){
        if (page == null){
            return "error/error_404";
        }
        JqGridData jqGridData = JqGridData.generateJqGridData(page);
        request.setAttribute("blogsPage",jqGridData);
        request.setAttribute("categories", categoryService.getAllCategories());
        request.setAttribute("pageName", pageName);
        request.setAttribute("pageUrl", pageUrl);
        request.setAttribute("keyword", keyword);
//        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
//        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/amaze/list";
    }

}
