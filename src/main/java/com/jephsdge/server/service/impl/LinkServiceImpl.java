package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.mapper.LinkMapper;
import com.jephsdge.server.pojo.Link;
import com.jephsdge.server.service.ILinkService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Autowired
    private LinkMapper linkMapper;
    @Override
    public long getLinksCount() {
        return linkMapper.selectCount(
                new QueryWrapper<Link>()
                        .eq("is_delete",false)
        );
    }

    @Override
    public Page<Link> getLinkByPage(Page<Link> page) {
        return linkMapper.selectPage(page,new QueryWrapper<Link>()
                .eq("is_delete",false));
    }

    @Override
    public Link getLinkById(Integer id) {
        return linkMapper.selectOne(new QueryWrapper<Link>()
                .eq("link_id",id)
                .eq("is_delete",false));
    }

    @Override
    public boolean insert(Link link) {
        return linkMapper.insert(link)==1;
    }

    @Override
    public boolean deleteById(Integer id) {
        Link link = new Link();
        link.setLinkId(id);
        link.setIsDelete(true);
        link.setDeleteTime(LocalDateTime.now());
        return linkMapper.updateById(link)==1;
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        boolean flag = true;
        for (Integer id:ids){
            flag &= deleteById(id);
        }
        return flag;
    }

    @Override
    public Map<Integer, List<Link>> getLinksMap() {
        List<Link> links = linkMapper.selectList(new QueryWrapper<Link>()
                .eq("is_delete", false));
        Map<Integer,List<Link>> map = new HashMap<>();
        List<Link> link_0 = new ArrayList<>();
        List<Link> link_1 = new ArrayList<>();
        List<Link> link_2 = new ArrayList<>();
        for (Link link:links){
            if (link.getLinkType()==0){
                link_0.add(link);
            }else if (link.getLinkType()==1){
                link_1.add(link);
            }else if (link.getLinkType()==2){
                link_2.add(link);
            }
        }
        map.put(0,link_0);
        map.put(1,link_1);
        map.put(2,link_2);
        return map;
    }
}
