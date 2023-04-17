package com.jephsdge.server.controller.admin;

import cn.hutool.captcha.ShearCaptcha;
import com.jephsdge.server.pojo.Admin;
import com.jephsdge.server.pojo.Message;
import com.jephsdge.server.service.*;
import com.jephsdge.server.utils.SessionAttributeName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Controller
@Api(tags = "管理员控制器")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBlogService blogService;
    @Autowired
    private ILinkService linkService;
    @Autowired
    private ITagService tagService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IConfigService configService;


    @ApiOperation("跳转登录页面")
    @GetMapping("/login")
    private String login(HttpSession session){
        session.removeAttribute(SessionAttributeName.LOGIN_ID);
        session.removeAttribute(SessionAttributeName.LOGIN_Nick_Name);
        session.removeAttribute(SessionAttributeName.ERRORMSG);
        return "admin/login";
    }

    @ApiOperation("登录请求")
    @PostMapping("/login")
    private String dologin(@RequestParam("userName") String userName,
                           @RequestParam("password") String password,
                           @RequestParam("verifyCode") String verifyCode,
                           HttpSession session){

        if (!StringUtils.hasText(verifyCode)){
            session.setAttribute(SessionAttributeName.ERRORMSG,"验证码不能为空");
            return "admin/login";
        }
        if (!StringUtils.hasText(userName) && !StringUtils.hasText(password)){
            session.setAttribute(SessionAttributeName.ERRORMSG,"用户名或密码不能为空");
            return "admin/login";
        }
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute(SessionAttributeName.VERIFYCODE);
        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)){
            session.setAttribute(SessionAttributeName.ERRORMSG, "验证码错误");
            return "admin/login";
        }

        Admin admin = adminService.login(userName,password);
        if (admin != null){
            session.setAttribute(SessionAttributeName.LOGIN_Nick_Name,admin.getNickName());
            session.setAttribute(SessionAttributeName.LOGIN_ID,admin.getAdminId());
            session.removeAttribute(SessionAttributeName.ERRORMSG);
            return "redirect:/admin/index";
        }else {
            session.setAttribute(SessionAttributeName.ERRORMSG,"登录失败");
            return "admin/login";
        }
    }

    @ApiOperation("后台主页")
    @GetMapping({"/","/index","","/index.html"})
    public String index(HttpServletRequest request){
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", categoryService.getCategorysCount());
        request.setAttribute("blogCount", blogService.getBlogsCount());
        request.setAttribute("linkCount", linkService.getLinksCount());
        request.setAttribute("tagCount", tagService.getTagsCount());
        request.setAttribute("commentCount", commentService.getCommentsCount());
        return "admin/index";
    }
    @ApiOperation("修改个人信息页")
    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        Integer loginId = (int) request.getSession().getAttribute(SessionAttributeName.LOGIN_ID);
        Admin adminUser = adminService.getAdminById(loginId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginName", adminUser.getLoginName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @ApiOperation("修改登录名和昵称")
    @PostMapping("/profile/name")
    @ResponseBody
    public Message uptateName(HttpSession session, @RequestParam("loginName") String loginName,
                              @RequestParam("nickName") String nickName){
        if (!StringUtils.hasText(loginName) || !StringUtils.hasText(nickName)){
            return Message.error("参数不能为空");
        }
        Integer loginId = (Integer)session.getAttribute(SessionAttributeName.LOGIN_ID);
        if (adminService.updateName(loginId,loginName,nickName)){
            session.removeAttribute(SessionAttributeName.LOGIN_Nick_Name);
            session.setAttribute(SessionAttributeName.LOGIN_Nick_Name,nickName);
            return Message.success("修改成功");
        }
        return Message.error("修改失败");
    }

    @ApiOperation("")
    @PostMapping("/profile/password")
    @ResponseBody
    public Message updatePassword(
            HttpSession session,
            @RequestParam("originalPassword") String originalPassword,
            @RequestParam("newPassword") String newPassword){
        if (!StringUtils.hasText(originalPassword) || !StringUtils.hasText(newPassword)){
            return Message.error("密码不能为空");
        }
        Integer loginId = (Integer)session.getAttribute(SessionAttributeName.LOGIN_ID);
        if (adminService.updatePassword(loginId,originalPassword,newPassword)){
            return Message.success("修改成功");
        }
        return Message.error("修改失败");
    }



    @ApiOperation("登出")
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(SessionAttributeName.LOGIN_Nick_Name);
        session.removeAttribute(SessionAttributeName.LOGIN_ID);
        session.removeAttribute(SessionAttributeName.ERRORMSG);
        return "admin/login";
    }

}
