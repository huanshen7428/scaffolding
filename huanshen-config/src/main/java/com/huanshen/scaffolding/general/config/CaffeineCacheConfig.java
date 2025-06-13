package com.huanshen.scaffolding.general.config;

import com.huanshen.scaffolding.common.cache.CommonCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CaffeineCacheConfig
 *
 */
@Slf4j
@Configuration
@EnableCaching
public class CaffeineCacheConfig {
    @Bean("defaultCacheManager")
    public CommonCacheManager defaultCacheManager() {
        CommonCacheManager defaultCacheManager = new CommonCacheManager();
        defaultCacheManager.setAllowNullValues(true);

        log.info("the caffeine cache manager is loaded successfully!");
        return defaultCacheManager;
    }
}
