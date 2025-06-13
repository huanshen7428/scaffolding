package com.huanshen.scaffolding.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2023/8/16.
 *
 */
@Slf4j
public class RedisLock {
    private RedisLock() {
    }
    private RedissonClient redissonClient;

    public RedisLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean tryLock(String lockId) {
        log.debug("start tryLock :{}", lockId);
        RLock lock = redissonClient.getLock(lockId);
        boolean result = lock.tryLock();
        log.debug("end tryLock :{}", result);
        return result;
    }

    public void lock(String lockId) {
        log.debug("start Lock :{}", lockId);
        RLock lock = redissonClient.getLock(lockId);
        lock.lock();
    }

    public boolean tryLock(String lockId, long time, TimeUnit unit) throws InterruptedException {
        log.debug("start tryLock :{},{}", lockId, time);
        RLock lock = redissonClient.getLock(lockId);
        boolean result = lock.tryLock(time, unit);
        log.debug("end tryLock :{}", result);
        return result;
    }

    public void unLock(String lockId) {
        try {
            RLock lock = redissonClient.getLock(lockId);
            lock.unlock();
            log.debug("release lock {}", lockId);
        } catch (Exception e) {
            log.debug("current thread cannot release lock, ignore this");
        }

    }

    public boolean hasLock(String lockId) {
        log.debug("start hasLock :{}", lockId);
        RLock lock = redissonClient.getLock(lockId);
        log.debug("finish hasLock :{}", lockId);
        return lock.isLocked();
    }

}
