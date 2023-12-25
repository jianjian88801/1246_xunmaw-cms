package com.xunmaw.cms.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页对象
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Pagination<T> extends Page<T> {

    private static final long serialVersionUID = 5194933845448697148L;

    public Pagination(long current, long size) {
        super(current, size);
    }

    //总页数
    private long pages;

    //前一页
    private long prePage;
    //下一页
    private long nextPage;

    //是否为第一页
    private boolean isFirstPage;
    //是否为最后一页
    private boolean isLastPage;
    //是否有前一页
    private boolean hasPreviousPage;
    //是否有下一页
    private boolean hasNextPage;

    @Override
    public Pagination<T> setTotal(long total) {
        super.setTotal(total);
        pages = super.getPages();
        long current = getCurrent();
        isFirstPage = current == 1;
        isLastPage = current == pages || pages == 0;
        hasPreviousPage = current > 1;
        hasNextPage = current < pages;
        if (current > 1) {
            prePage = current - 1;
        }
        if (current < pages) {
            nextPage = current + 1;
        }
        return this;
    }
}