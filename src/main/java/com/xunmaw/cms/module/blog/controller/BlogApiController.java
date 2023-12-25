package com.xunmaw.cms.module.blog.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.util.*;
import com.xunmaw.cms.module.admin.model.BizArticleLook;
import com.xunmaw.cms.module.admin.model.BizComment;
import com.xunmaw.cms.module.admin.model.BizLove;
import com.xunmaw.cms.module.admin.service.BizArticleLookService;
import com.xunmaw.cms.module.admin.service.BizCommentService;
import com.xunmaw.cms.module.admin.service.BizLoveService;
import com.xunmaw.cms.module.admin.vo.CommentConditionVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 给前台页面提供的接口，包括针对文章进行的添加评论、获取评论、增加浏览次数和点赞操作
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@RestController
@RequestMapping("blog/api")
@AllArgsConstructor
public class BlogApiController {

    private final BizCommentService commentService;
    private final BizArticleLookService articleLookService;
    private final BizLoveService loveService;


    @PostMapping("comments")
    public IPage<BizComment> getComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        return commentService.selectComments(vo, pageNumber, pageSize);
    }

    @PostMapping("comment/save")
    public ResponseVo saveComment(HttpServletRequest request, BizComment comment) throws UnsupportedEncodingException {
        if (StrUtil.isEmpty(comment.getNickname())) {
            return ResultUtil.error("请输入昵称");
        }
        if (StrUtil.isNotBlank(comment.getEmail()) && !Validator.isEmail(comment.getEmail())) {
            return ResultUtil.error("邮箱格式不正确");
        }
        Date date = new Date();
        comment.setNickname(HtmlUtil.filter(comment.getNickname()));
        comment.setContent(HtmlUtil.filter(comment.getContent()));
        comment.setIp(HtmlUtil.filter(IpUtil.getIpAddr(request)));
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        if (StrUtil.isNotBlank(comment.getQq())) {
            comment.setAvatar("http://q1.qlogo.cn/g?b=qq&nk=" + comment.getQq() + "&s=100");
        } else if (StrUtil.isNotBlank(comment.getEmail())) {
            String entry = null;
            try {
                entry = MD5.md5Hex(comment.getEmail());
            } catch (NoSuchAlgorithmException e) {
                log.error("MD5出现异常{}", e.getMessage(), e);
            }
            comment.setAvatar("http://www.gravatar.com/avatar/" + entry + "?d=mp");
        }
        boolean a = commentService.save(comment);
        if (a) {
            return ResultUtil.success("评论提交成功,系统正在审核");
        } else {
            return ResultUtil.error("评论提交失败");
        }
    }


    @PostMapping("article/look")
    public ResponseVo checkLook(HttpServletRequest request, Integer articleId) {
        /*浏览次数*/
        Date date = new Date();
        String ip = IpUtil.getIpAddr(request);
        int checkCount = articleLookService.checkArticleLook(articleId, ip, DateUtil.addHours(date, -1));
        if (checkCount == 0) {
            BizArticleLook articleLook = new BizArticleLook();
            articleLook.setArticleId(articleId);
            articleLook.setUserIp(ip);
            articleLook.setLookTime(date);
            articleLook.setCreateTime(date);
            articleLook.setUpdateTime(date);
            articleLookService.save(articleLook);
            return ResultUtil.success("浏览次数+1");
        } else {
            return ResultUtil.error("一小时内重复浏览不增加次数哦");
        }
    }


    @PostMapping("love")
    public ResponseVo love(HttpServletRequest request, Integer bizId, Integer bizType) {
        Date date = new Date();
        String ip = IpUtil.getIpAddr(request);
        BizLove bizLove = loveService.checkLove(bizId, ip);
        if (bizLove == null) {
            bizLove = new BizLove();
            bizLove.setBizId(bizId);
            bizLove.setBizType(bizType);
            bizLove.setUserIp(ip);
            bizLove.setStatus(CoreConst.STATUS_VALID);
            bizLove.setCreateTime(date);
            bizLove.setUpdateTime(date);
            loveService.save(bizLove);
            return ResultUtil.success("点赞成功");
        } else {
            return ResultUtil.error("您已赞过了哦~");
        }
    }

}
