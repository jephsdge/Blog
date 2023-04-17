package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.pojo.Link;

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
public interface ILinkService extends IService<Link> {

    long getLinksCount();

    Page<Link> getLinkByPage(Page<Link> page);

    Link getLinkById(Integer id);

    boolean insert(Link link);

    boolean deleteById(Integer id);

    boolean deleteBatch(Integer[] ids);

    Map<Integer, List<Link>> getLinksMap();
}
