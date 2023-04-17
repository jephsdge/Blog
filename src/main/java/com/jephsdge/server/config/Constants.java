package com.jephsdge.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * Created by 13 on 2019/5/24.
 */
@Component
public class Constants {
//    public final static String FILE_UPLOAD_DIC = Objects.requireNonNull(Constants.class.getResource("/")).getPath() + "static/admin/blogimg/";//上传文件的默认url前缀，根据部署设置自行修改
//    public final static String FILE_UPLOAD_DIC = "/www/wwwroot/blog.jephsdge.cn/BlogImg/";//上传文件的默认url前缀，根据部署设置自行修改
//    public final static String FILE_UPLOAD_DIC = "C:/Users/jephs/Desktop/MyPro/IDEA/Blog/Blog-server/target/img";//上传文件的默认url前缀，根据部署设置自行修改

    @Value("${file-path}")
    private String filePath;

    public String getPath(){
        ApplicationHome home = new ApplicationHome(getClass());
        return home.getSource().getParentFile().getAbsolutePath()+filePath;
    }
}
