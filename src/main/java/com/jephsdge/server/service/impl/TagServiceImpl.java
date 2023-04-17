package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.mapper.TagMapper;
import com.jephsdge.server.pojo.Tag;
import com.jephsdge.server.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;
    @Override
    public long getTagsCount() {
        return tagMapper.selectCount(
                new QueryWrapper<Tag>()
                        .eq("is_delete",false)
        );
    }

    @Override
    public Page<Tag> getTagsByPage(Page<Tag> page) {
        return tagMapper.selectPage(page,new QueryWrapper<Tag>()
                .eq("is_delete",false));
    }

    @Override
    public boolean insert(Tag tag) {
        return tagMapper.insert(tag) == 1;
    }

    @Override
    public boolean deleteById(Long id) {
        Tag tag = new Tag();
        tag.setTagId(id);
        tag.setIsDelete(true);
        tag.setDeleteTime(LocalDateTime.now());
        return tagMapper.updateById(tag) == 1;
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        boolean flag = true;
        for (Long id : ids){
            flag &= deleteById(id);
        }
        return flag;
    }
}
