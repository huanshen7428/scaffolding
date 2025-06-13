package com.huanshen.scaffolding.common.page.util;

import com.huanshen.scaffolding.common.page.domain.BasePageRequest;
import com.huanshen.scaffolding.common.page.domain.TableSupport;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        BasePageRequest pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
//        Boolean reasonable = pageDomain.getReasonable();
//        PageHelper.startPage(pageNum, pageSize).setReasonable(reasonable);
        PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 针对post请求，设置分页参数
     * @param pageNum
     * @param pageSize
     */
    public static void setPage(int pageNum, int pageSize) {
        ServletUtils.setAttribute(TableSupport.PAGE_NUM, pageNum);
        ServletUtils.setAttribute(TableSupport.PAGE_SIZE, pageSize);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }

    /**
     * 根据pageList，将resList转换为Page对象
     *
     * @param pageList
     * @param resList
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> List<T> copyPageList(List<S> pageList, List<T> resList) {
        if (pageList == null || pageList.isEmpty()) {
            return Collections.emptyList();
        }
        long total = pageList.size();
        if (pageList instanceof Page) {
            Page page = (Page) pageList;
            total = page.getTotal();
        }
        Page<T> newPage = new Page<>();
        newPage.setTotal(total);
        newPage.addAll(resList);
        return newPage;
    }
}
