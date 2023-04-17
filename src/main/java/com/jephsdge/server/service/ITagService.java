package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.pojo.Tag;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
public interface ITagService extends IService<Tag> {

    long getTagsCount();

    Page<Tag> getTagsByPage(Page<Tag> page);

    boolean insert(Tag tag);

    boolean deleteById(Long id);

    boolean deleteBatch(Long[] ids);
}
