package com.jephsdge.server.controller.common;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "验证码控制器")
@Controller
public class CommonController {
    @ApiOperation(value = "获取验证码")
    @GetMapping("/common/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/png");

        ShearCaptcha shearCaptcha= CaptchaUtil.createShearCaptcha(150, 30, 4, 2);

        // 验证码存入session
        httpServletRequest.getSession().setAttribute("verifyCode", shearCaptcha);

        // 输出图片流
        shearCaptcha.write(httpServletResponse.getOutputStream());
    }
}
