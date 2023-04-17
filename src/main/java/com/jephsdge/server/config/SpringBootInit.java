package com.jephsdge.server.config;

import com.jephsdge.server.mapper.ConfigMapper;
import com.jephsdge.server.pojo.Config;
import com.jephsdge.server.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class SpringBootInit implements ApplicationRunner {

    @Autowired
    private IConfigService configService;

    @Value("${webConfig.websiteName}")
    private String websiteName;

    @Value("${webConfig.websiteDescription}")
    private String websiteDescription;

    @Value("${webConfig.websiteLogo}")
    private String websiteLogo;

    @Value("${webConfig.websiteIcon}")
    private String websiteIcon;

    @Value("${webConfig.personalAvatar}")
    private String personalAvatar;
    @Value("${webConfig.personalEmail}")
    private String personalEmail;

    @Value("${webConfig.personalName}")
    private String personalName;

    @Value("${webConfig.footerAbout}")
    private String footerAbout;

    @Value("${webConfig.footerICP}")
    private String footerICP;

    @Value("${webConfig.footerCopyRight}")
    private String footerCopyRight;

    @Value("${webConfig.footerPoweredBy}")
    private String footerPoweredBy;

    @Value("${webConfig.footerPoweredByURL}")
    private String footerPoweredByURL;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Config> map = configService.getConfigMap();
        init(map);
    }

    private void init(Map<String, Config> map) throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field:fields){
            if (!"configService".equals(field.getName())){
                if (map.get(field.getName()) == null){
                    configService.insert(generateConfig(field.getName(), field.get(this).toString()));
                }
            }

        }

    }

    private Config generateConfig(String configName,String configValue){
        Config config = new Config();
        config.setConfigName(configName);
        config.setConfigValue(configValue);
        return config;
    }
    private Config generateConfigById(Config config, String configValue){
        config.setConfigValue(configValue);
        return config;
    }
}
