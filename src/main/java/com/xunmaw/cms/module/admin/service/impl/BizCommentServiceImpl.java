package com.xunmaw.cms.module.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmaw.cms.common.util.Pagination;
import com.xunmaw.cms.module.admin.mapper.BizCommentMapper;
import com.xunmaw.cms.module.admin.model.BizComment;
import com.xunmaw.cms.module.admin.service.BizCommentService;
import com.xunmaw.cms.module.admin.vo.CommentConditionVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class BizCommentServiceImpl extends ServiceImpl<BizCommentMapper, BizComment> implements BizCommentService {

    private final BizCommentMapper commentMapper;

    @Override
    public IPage<BizComment> selectComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        IPage<BizComment> page = new Pagination<>(pageNumber, pageSize);
        page.setRecords(commentMapper.selectComments(page, vo));
        return page;
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return commentMapper.deleteBatch(ids);
    }
}
