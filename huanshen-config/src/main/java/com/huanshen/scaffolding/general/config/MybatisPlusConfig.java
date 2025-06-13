package com.huanshen.scaffolding.general.config;

import com.huanshen.scaffolding.common.utils.MonthlyTableHelper;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            try {
                // 先查是否按月分表
                String monthTable = MonthlyTableHelper.get();
                if (StringUtils.isNotEmpty(monthTable)) {
                    if (monthTable.contains(tableName)) {
                        tableName = monthTable;
                    } else {
                        tableName = tableName + monthTable;
                    }
                }
                return tableName;
            } catch (Exception e) {
                return tableName;
            }
        });
        mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);


        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //乐观锁
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
