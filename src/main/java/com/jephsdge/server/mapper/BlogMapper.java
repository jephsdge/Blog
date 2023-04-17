package com.jephsdge.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jephsdge.server.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
