package com.huanshen.scaffolding.common.page.domain;


import cn.hutool.core.convert.Convert;
import com.huanshen.scaffolding.common.page.util.ServletUtils;

/**
 * 表格数据处理
 *
 */
public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static BasePageRequest getPageDomain() {
        BasePageRequest pageDomain = new BasePageRequest();

        Object pageNumAttr = ServletUtils.getAttribute(PAGE_NUM);
        Object pageSizeAttr = ServletUtils.getAttribute(PAGE_SIZE);

        pageDomain.setPageNum(Convert.toInt(pageNumAttr == null ? ServletUtils.getParameter(PAGE_NUM) : pageNumAttr, 1));
        pageDomain.setPageSize(Convert.toInt(pageSizeAttr == null ? ServletUtils.getParameter(PAGE_SIZE) : pageSizeAttr, 10));
//        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static BasePageRequest buildPageRequest() {
        return getPageDomain();
    }
}
