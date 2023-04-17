package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.mapper.ConfigMapper;
import com.jephsdge.server.pojo.Config;
import com.jephsdge.server.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

    @Autowired
    private ConfigMapper configMapper;
    @Override
    public Map<String,String> getAllConfigs() {
        List<Config> configs = configMapper.selectList(null);
        Map<String,String> map = new HashMap<>();
        for (Config config:configs){
            String configName = config.getConfigName();
            String configValue = config.getConfigValue();
            map.put(configName,configValue);
        }
        return map;
    }

    @Override
    public boolean insert(Config config) {
        return configMapper.insert(config)==1;
    }

    @Override
    public Config getConfigByName(String configName) {
        return configMapper.selectOne(new QueryWrapper<Config>()
                .eq("config_name",configName));
    }

    @Override
    public Map<String, Config> getConfigMap() {
        List<Config> configs = configMapper.selectList(null);
        Map<String, Config> map = new HashMap<>();
        for (Config config:configs){
            map.put(config.getConfigName(), config);
        }
        return map;
    }

    @Override
    public boolean updateByKV(String configName, String configValue) {
        Config config = getConfigByName(configName);
        config.setConfigValue(configValue);
        return configMapper.updateById(config)==1;
    }
}
