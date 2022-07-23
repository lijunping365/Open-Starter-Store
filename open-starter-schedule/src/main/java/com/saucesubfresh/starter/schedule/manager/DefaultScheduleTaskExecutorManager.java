package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 李俊平
 * @Date: 2022-07-23 13:46
 */
@Slf4j
public class DefaultScheduleTaskExecutorManager implements ScheduleTaskExecutorManager {

    private final ConcurrentMap<String, ScheduleTaskExecutor> executorMap = new ConcurrentHashMap<>();

    @Override
    public void put(String scheduleName, ScheduleTaskExecutor executor) {
        executorMap.put(scheduleName,executor);
    }

    @Override
    public ScheduleTaskExecutor getTaskExecutor(String scheduleName) {
        return executorMap.get(scheduleName);
    }
}
