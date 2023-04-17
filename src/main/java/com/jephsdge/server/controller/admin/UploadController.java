package com.jephsdge.server.controller.admin;

import com.jephsdge.server.config.Constants;
import com.jephsdge.server.pojo.MdImg;
import com.jephsdge.server.pojo.Message;
import com.jephsdge.server.utils.MyBlogUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class UploadController {

    @Autowired
    private Constants constants;

    @PostMapping({"/upload/file"})
    @ResponseBody
    public Message upload(@RequestParam("file") MultipartFile file){
        try {
            Message message = uploadFile(file);
            if (message.getCode() != 200){
                return Message.error("上传失败!");
            }else {
                return message;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Message.error("上传失败");
        }
    }

    @ApiOperation("Markdown上传图片")
    @PostMapping("/blogs/md/uploadfile")
    @ResponseBody
    public MdImg uploadFileByEditormd(@RequestParam(name = "editormd-image-file", required = true)
                                      MultipartFile file){
        /*String fileName = file.getOriginalFilename();
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
        File fileDirectory = new File(constants.getPath());
        System.out.println(fileDirectory);
        System.out.println(fileDirectory.isDirectory());
        System.out.println(fileDirectory.isFile());
        //创建文件
        File destFile = new File(constants.getPath() + newFileName);
        try {
            if (!fileDirectory.exists() && fileDirectory.isDirectory()) {
                if (!fileDirectory.mkdir()) {
                    return new MdImg(0,"文件上传错误!",null);
//                    System.out.println("文件上传失败");
                }
            }
//            FileUtils.copyInputStreamToFile(file.getInputStream(),destFile);
            file.transferTo(destFile);
            return new MdImg(1,"文件上传成功","/upload/" + newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new MdImg(0,"文件上传错误",null);
        }*/

        try {
            Message message = uploadFile(file);
            if (message.getCode() != 200){
                return new MdImg(0,"文件上传错误!",null);
            }else {
                return new MdImg(1,"文件上传成功",message.getData().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new MdImg(0,"文件上传错误",null);
        }
    }

    private Message uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
        File fileDirectory = new File(constants.getPath());
        if (fileDirectory.exists()){
            if (!fileDirectory.isDirectory()) {
                return Message.error("存在和文件夹同名文件");
            }
        }else {
            if (!fileDirectory.mkdir()) {
                return Message.error("文件夹创建失败");
            }
        }
        //创建文件
        File destFile = new File(constants.getPath() + newFileName);
        file.transferTo(destFile);
        return Message.success("上传成功","/upload/" + newFileName);
    }
}
