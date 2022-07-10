package com.saucesubfresh.starter.schedule.executor;

import java.util.List;

/**
 * <p>
 *  调度任务执行器
 *
 *  建议使用多线程执行防止任务发生阻塞
 * </p>
 *
 * @author lijunping on 2022/1/20
 */
public interface ScheduleTaskExecutor {

    void execute(List<Long> taskList);
}
