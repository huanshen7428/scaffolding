package com.huanshen.scaffolding.common.utils;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2024/3/19.
 *
 */
public class RedisDelayQueue {

    private RedissonClient redissonClient;

    public static final String QUEUE_NAME = "smsgw:send:delay:queue";


    public RedisDelayQueue() {
    }

    public RedisDelayQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    public  void addQueue(String sid, long delay, TimeUnit timeUnit) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_NAME);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(sid, delay, timeUnit);
    }

    public RBlockingDeque<String> getQueue() {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_NAME);
        redissonClient.getDelayedQueue(blockingDeque);
        return blockingDeque;
    }

    public void removeQueue(String sid) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_NAME);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.remove(sid);
    }

    public boolean containsQueue(String sid) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_NAME);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        return delayedQueue.contains(sid);
    }

}
