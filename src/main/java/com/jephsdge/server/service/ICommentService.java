package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.pojo.Blog;
import com.jephsdge.server.pojo.Comment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
public interface ICommentService extends IService<Comment> {

    long getCommentsCount();

    Page<Comment> getCommentsByPage(Page<Comment> page);

    boolean checkCommentById(Long id);
    boolean checkBatch(Long[] ids);

    boolean deleteCommentById(Long id);
    boolean deleteBatch(Long[] ids);

    boolean deleteCommentByBlogId(Long blogId);

    boolean reply(Long commentId, String replyBody);

    Long getCommetnCountByBlogId(Long blogId);

    Page<Comment> getCommentPageByBlogIdAndPage(Long blogId, Page<Comment> page);

    boolean addComment(Comment comment);
}
