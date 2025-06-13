package com.huanshen.scaffolding.business.common.service.impl;

import com.huanshen.scaffolding.business.common.service.ICommonService;
import com.huanshen.scaffolding.common.cache.annotations.CommonCacheable;
import com.huanshen.scaffolding.common.cache.enums.CacheGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * CommonServiceImpl
 *
 */
@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {
    @Override
    @CommonCacheable(group = CacheGroup.SYSTEM, sync = true)
    public int test() {
        log.info("====do cache");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return LocalDateTime.now().getSecond();
    }
}
