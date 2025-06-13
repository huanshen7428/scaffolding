package com.huanshen.scaffolding.common.page.aspect;

import cn.hutool.core.convert.Convert;
import com.huanshen.scaffolding.common.page.domain.TableSupport;
import com.huanshen.scaffolding.common.page.util.ServletUtils;
import com.huanshen.scaffolding.common.utils.ArrayUtil;
import com.huanshen.scaffolding.common.utils.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * PageAspect
 *
 * @description 分页aop
 */
@Slf4j
@Component
@Aspect
public class PageAspect {

    /**
     * 拦截controller的请求，如果http请求方式为post，则取入参
     * 反射获取pageNum和pageSize，并注入到attribute中
     * 分页时，从attribute中取值，如果取不到，再查询parameter
     * <p>
     * 针对post场景。 让post能直接使用PageUtils.startPage()方法
     *
     * @param point
     */
    @Before("@annotation(com.huanshen.scaffolding.common.page.annotation.CommonPage)")
    public void before(JoinPoint point) {
//        MethodSignature methodSignature = (MethodSignature) point.getSignature();

//        Method method = methodSignature.getMethod();

//        PostMapping postMapping = method.getAnnotation(PostMapping.class);
//        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        Integer pageNum = null;
        Integer pageSize = null;

        Object pageNumAttr = ServletUtils.getParameter(TableSupport.PAGE_NUM);
        Object pageSizeAttr = ServletUtils.getParameter(TableSupport.PAGE_SIZE);
        if (pageNumAttr != null) {
            pageNum = Convert.toInt(pageNumAttr, 1);
            pageSize = Convert.toInt(pageSizeAttr, 10);
        } else {
            Object[] args = point.getArgs();
            for (Object arg : args) {
                if (arg == null) {
                    continue;
                }
                Object pageNumObj = ReflectUtil.getValueByField(arg, TableSupport.PAGE_NUM);
                pageNum = pageNumObj == null ? null : Convert.toInt(pageNumObj);

                Object pageSizeObj = ReflectUtil.getValueByField(arg, TableSupport.PAGE_SIZE);
                pageSize = pageSizeObj == null ? null : Convert.toInt(pageSizeObj);

                if (pageNum != null) {
                    break;
                }
            }
        }
        ServletUtils.setAttribute(TableSupport.PAGE_NUM, pageNum);
        ServletUtils.setAttribute(TableSupport.PAGE_SIZE, pageSize);

//        if (!checkPostMapping(postMapping) && !checkRequestMapping(requestMapping)) {
//            //get请求
//            Object pageNumAttr = ServletUtils.getParameter(PAGE_NUM);
//            Object pageSizeAttr = ServletUtils.getParameter(PAGE_SIZE);
//
//            pageNum = Convert.toInt(pageNumAttr, 1);
//            pageSize = Convert.toInt(pageSizeAttr, 10);
//        } else {
//            //post请求
//            Object[] args = point.getArgs();
//            for (Object arg : args) {
//                if (arg == null) {
//                    continue;
//                }
//                Object pageNumObj = ReflectUtil.getValueByField(arg, PAGE_NUM);
//                pageNum = pageNumObj == null ? null : Convert.toInt(pageNumObj);
//
//                Object pageSizeObj = ReflectUtil.getValueByField(arg, PAGE_SIZE);
//                pageSize = pageSizeObj == null ? null : Convert.toInt(pageSizeObj);
//
//                if (pageNum != null) {
//                    break;
//                }
//            }
//        }
//        ServletUtils.setAttribute(PAGE_NUM, pageNum);
//        ServletUtils.setAttribute(PAGE_SIZE, pageSize);
    }

    private boolean checkPostMapping(PostMapping postMapping) {
        return postMapping != null;
    }

    private boolean checkRequestMapping(RequestMapping requestMapping) {
        return requestMapping != null && ArrayUtil.contains(requestMapping.method(), RequestMethod.POST);
    }
}
