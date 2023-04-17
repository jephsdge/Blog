package com.jephsdge.server.controller.admin;

import com.jephsdge.server.pojo.Message;
import com.jephsdge.server.service.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
@Api(tags = "系统配置模块")
@RequestMapping("/admin")
public class ConfigController {

    @Autowired
    private IConfigService configService;

    @ApiOperation("")
    @GetMapping("/configurations")
    public String config(HttpServletRequest request){
        request.setAttribute("path","configurations");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "admin/configuration";
    }

    @ApiOperation("")
    @PostMapping("/configurations/website")
    @ResponseBody
    public Message configWebsite(
            @RequestParam String websiteName,
            @RequestParam String websiteDescription,
            @RequestParam String websiteLogo,
            @RequestParam String websiteIcon
    ){
        if (!StringUtils.hasText(websiteName) ||
        !StringUtils.hasText(websiteDescription)||
        !StringUtils.hasText(websiteLogo)||
        !StringUtils.hasText(websiteIcon)){
            return Message.error("参数错误");
        }
        if (
                configService.updateByKV("websiteName",websiteName) &&
                configService.updateByKV("websiteDescription",websiteDescription) &&
                configService.updateByKV("websiteLogo",websiteLogo) &&
                configService.updateByKV("websiteIcon",websiteIcon)
        ){
            return Message.success("修改成功");
        }

        return Message.error("修改失败");
    }

    @ApiOperation("")
    @PostMapping("/configurations/userInfo")
    @ResponseBody
    public Message configUserInfo(
            @RequestParam String personalAvatar,
            @RequestParam String personalName,
            @RequestParam String personalEmail
    ){
        if (!StringUtils.hasText(personalAvatar) || !StringUtils.hasText(personalName) || !StringUtils.hasText(personalEmail)
        ){
            return Message.error("参数错误");
        }
        if (
                configService.updateByKV("personalAvatar",personalAvatar) &&
                configService.updateByKV("personalName",personalName) &&
                configService.updateByKV("personalEmail",personalEmail)
        ){
            return Message.success("修改成功");
        }

        return Message.error("修改失败");
    }

    @ApiOperation("")
    @PostMapping("/configurations/footer")
    @ResponseBody
    public  Message configFooter(
            @RequestParam String footerAbout,
            @RequestParam String footerICP,
            @RequestParam String footerCopyRight,
            @RequestParam String footerPoweredBy,
            @RequestParam String footerPoweredByURL
    ){
        if (
                !StringUtils.hasText(footerAbout) ||
                !StringUtils.hasText(footerICP) ||
                !StringUtils.hasText(footerCopyRight) ||
                !StringUtils.hasText(footerPoweredBy) ||
                !StringUtils.hasText(footerPoweredByURL)
        ){
            return Message.error("参数错误");
        }
        if (
                configService.updateByKV("footerAbout",footerAbout) &&
                configService.updateByKV("footerICP",footerICP) &&
                configService.updateByKV("footerCopyRight",footerCopyRight) &&
                configService.updateByKV("footerPoweredBy",footerPoweredBy) &&
                configService.updateByKV("footerPoweredByURL",footerPoweredByURL)

        ){
            return Message.success("修改成功");
        }

        return Message.error("修改失败");
    }

}
