package com.xunmaw.cms.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.IpUtil;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.model.BizComment;
import com.xunmaw.cms.module.admin.model.User;
import com.xunmaw.cms.module.admin.service.BizCommentService;
import com.xunmaw.cms.module.admin.vo.CommentConditionVo;
import com.xunmaw.cms.module.admin.vo.base.PageResultVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台评论管理
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@RestController
@RequestMapping("comment")
@AllArgsConstructor
public class CommentController {

    private final BizCommentService commentService;

    @PostMapping("list")
    public PageResultVo loadNotify(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {

        IPage<BizComment> commentPage = commentService.selectComments(vo, pageNumber, pageSize);
        return ResultUtil.table(commentPage.getRecords(), commentPage.getTotal());
    }

    @PostMapping("/reply")
    public ResponseVo edit(BizComment comment) {
        completeComment(comment);
        boolean i = commentService.save(comment);
        if (i) {
            return ResultUtil.success("回复评论成功");
        } else {
            return ResultUtil.error("回复评论失败");
        }
    }

    @PostMapping("/delete")
    public ResponseVo delete(Integer id) {
        Integer[] ids = {id};
        int i = commentService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除评论成功");
        } else {
            return ResultUtil.error("删除评论失败");
        }
    }

    @PostMapping("/batch/delete")
    public ResponseVo deleteBatch(@RequestParam("ids[]") Integer[] ids) {
        int i = commentService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除评论成功");
        } else {
            return ResultUtil.error("删除评论失败");
        }
    }

    @PostMapping("/audit")
    public ResponseVo audit(BizComment bizComment, String replyContent) {
        try {
            commentService.updateById(bizComment);
            if (StrUtil.isNotBlank(replyContent)) {
                BizComment replyComment = new BizComment();
                replyComment.setPid(bizComment.getId());
                replyComment.setSid(bizComment.getSid());
                replyComment.setContent(replyContent);
                completeComment(replyComment);
                commentService.save(replyComment);
            }
            return ResultUtil.success("审核成功");
        } catch (Exception e) {
            return ResultUtil.success("审核失败");
        }
    }

    private void completeComment(BizComment comment) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        comment.setUserId(user.getUserId());
        comment.setNickname(user.getNickname());
        comment.setEmail(user.getEmail());
        comment.setAvatar(user.getImg());
        comment.setIp(IpUtil.getIpAddr(request));
        comment.setStatus(CoreConst.STATUS_VALID);
    }

}
