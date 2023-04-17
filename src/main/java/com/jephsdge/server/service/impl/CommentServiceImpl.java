package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.mapper.BlogMapper;
import com.jephsdge.server.mapper.CommentMapper;
import com.jephsdge.server.pojo.Blog;
import com.jephsdge.server.pojo.Comment;
import com.jephsdge.server.pojo.MyRuntimeException;
import com.jephsdge.server.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Override
    public long getCommentsCount() {
        return commentMapper.selectCount(
                new QueryWrapper<Comment>()
                        .eq("is_delete",false)
        );
    }

    @Override
    public Page<Comment> getCommentsByPage(Page<Comment> page) {
        Page<Comment> commentPage = commentMapper.selectPage(page, new QueryWrapper<Comment>()
                .eq("is_delete", false));
        List<Comment> records = commentPage.getRecords();
        for (Comment comment:records){
            Long blogId = comment.getBlogId();
            String blogTitle = blogMapper.selectById(blogId).getBlogTitle();
            comment.setBlogTitle(blogTitle);
        }
        commentPage.setRecords(records);
        return commentPage;
    }

    @Override
    public boolean checkCommentById(Long id) {
        Comment comment = new Comment();
        comment.setCommentId(id);
        comment.setCommentStatus(true);
        return commentMapper.updateById(comment)==1;
    }


    @Override
    public boolean checkBatch(Long[] ids) {
        boolean flag = true;
        for (Long id:ids){
            flag &= checkCommentById(id);
        }
        return flag;
    }

    @Override
    public boolean deleteCommentById(Long id) {
        Comment comment = new Comment();
        comment.setCommentId(id);
        comment.setIsDelete(true);
        comment.setDeleteTime(LocalDateTime.now());
        return commentMapper.updateById(comment)==1;
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        boolean flag = true;
        for (Long id : ids){
            flag &= deleteCommentById(id);
        }
        return flag;
    }

    @Override
    public boolean deleteCommentByBlogId(Long blogId) {
        boolean flag = true;
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("blog_id", blogId));
        for (Comment comment:commentList){
            comment.setIsDelete(true);
            comment.setDeleteTime(LocalDateTime.now());
            flag &= commentMapper.updateById(comment)==1;
        }
        if (!flag){
            throw new MyRuntimeException("通过博客ID删除评论时出错");
        }
        return flag;
    }


    @Override
    public boolean reply(Long commentId, String replyBody) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setReplyBody(replyBody);
        comment.setReplyTime(LocalDateTime.now());
        return commentMapper.updateById(comment) == 1;
    }

    @Override
    public Long getCommetnCountByBlogId(Long blogId) {
        return commentMapper.selectCount(new QueryWrapper<Comment>()
                .eq("blog_id", blogId)
                .eq("comment_status",true)
                .eq("is_delete", false));
    }

    @Override
    public Page<Comment> getCommentPageByBlogIdAndPage(Long blogId, Page<Comment> page) {
        return commentMapper.selectPage(page, new QueryWrapper<Comment>()
                .eq("blog_id", blogId)
                .eq("comment_status",true)
                .eq("is_delete", false));
    }

    @Override
    public boolean addComment(Comment comment) {
        return commentMapper.insert(comment)==1;
    }
}
