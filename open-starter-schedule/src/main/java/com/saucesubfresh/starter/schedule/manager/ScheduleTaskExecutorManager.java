package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;

/**
 * 调度任务执行器的管理器
 *
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskExecutorManager {

    /**
     * 根据调度器名称获取调度任务的执行器
     *
     * @param scheduleName
     * @return
     */
    ScheduleTaskExecutor getTaskExecutor(String scheduleName);
}
