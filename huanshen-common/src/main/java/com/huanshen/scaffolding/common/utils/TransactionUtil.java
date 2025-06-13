package com.huanshen.scaffolding.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Function;

/**
 * TransactionUtil
 *
 * @date 2025-01-17 17:14
 */
public class TransactionUtil {
    private static PlatformTransactionManager transactionManager;

    private TransactionUtil() {
    }

    private static void checkBean() {
        if (transactionManager == null) {
            transactionManager = SpringUtil.getBean(PlatformTransactionManager.class);
        }
        if (transactionManager == null) {
            throw new RuntimeException("transactionManager is null");
        }
    }

    /**
     * 自动提交事务或回滚
     * @param name 事务名称
     * @param propagation 默认应为 TransactionDefinition.PROPAGATION_REQUIRED
     * @param func
     * @param param
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> R doCommit(String name, int propagation, Function<T, R> func, T param) {
        checkBean();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(name);
        def.setPropagationBehavior(propagation);
        TransactionStatus status = transactionManager.getTransaction(def);
        R result;
        try {
            result = func.apply(param);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        return result;
    }
}
