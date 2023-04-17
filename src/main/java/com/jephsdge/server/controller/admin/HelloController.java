package com.jephsdge.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试")
public class HelloController {

    @ApiOperation(value = "测试/hello")
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
