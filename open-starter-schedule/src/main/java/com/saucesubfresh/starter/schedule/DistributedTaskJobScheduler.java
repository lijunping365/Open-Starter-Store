package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskExecutorManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于分布式锁实现分布式调度,避免同一个任务在多个节点同时执行
 *
 * 注意: {@link RLock#tryLock(long, long, TimeUnit)}, 当 waitTime 不为 -1 的时候,
 *
 * 在尝试获取锁的时候会阻塞当前线程, 阻塞时长是 waitTime,
 *
 * 当 leaseTime 不为 -1 的时候, 当前线程会一直持有锁且阻塞当前线程到 leaseTime 为 0 时会自动释放锁
 *
 *
 * @author: 李俊平
 * @Date: 2022-07-16 11:49
 */
@Slf4j
public class DistributedTaskJobScheduler extends AbstractTaskJobScheduler {

    private final String lockName;
    private final RedissonClient redissonClient;

    public DistributedTaskJobScheduler(RedissonClient redissonClient,
                                       ScheduleProperties scheduleProperties,
                                       ScheduleTaskPoolManager scheduleTaskPoolManager,
                                       ScheduleTaskQueueManager scheduleTaskQueueManager,
                                       ScheduleTaskExecutorManager scheduleTaskExecutorManager) {
        super(scheduleTaskPoolManager, scheduleTaskQueueManager, scheduleTaskExecutorManager);
        this.redissonClient = redissonClient;
        String lockName = scheduleProperties.getLockName();
        if (StringUtils.isBlank(lockName)){
            throw new ScheduleException("The LockName cannot be empty.");
        }
        this.lockName = lockName;
    }

    @Override
    protected void runTask(List<Long> taskIds) throws Exception {
        RLock lock = redissonClient.getLock(lockName);
        long waitTime = 1000 - System.currentTimeMillis() % 1000;
        if (lock.tryLock(waitTime, waitTime, TimeUnit.MILLISECONDS)){
            super.executeTask(taskIds);
        }
    }
}
