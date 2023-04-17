package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.mapper.ConfigMapper;
import com.jephsdge.server.pojo.Config;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
public interface IConfigService extends IService<Config> {

    Map<String,String> getAllConfigs();
    boolean insert(Config config);
    Config getConfigByName(String configName);

    Map<String, Config> getConfigMap();

    boolean updateByKV(String configName, String configValue);
}
