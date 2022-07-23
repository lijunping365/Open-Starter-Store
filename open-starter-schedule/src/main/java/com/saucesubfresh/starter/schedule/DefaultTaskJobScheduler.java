package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-07-16 11:49
 */
@Slf4j
public class DefaultTaskJobScheduler extends AbstractTaskJobScheduler {

    private final ScheduleTaskExecutor scheduleTaskExecutor;

    public DefaultTaskJobScheduler(ScheduleTaskExecutor scheduleTaskExecutor,
                                   ScheduleTaskPoolManager scheduleTaskPoolManager,
                                   ScheduleTaskQueueManager scheduleTaskQueueManager) {
        super(scheduleTaskPoolManager, scheduleTaskQueueManager);
        this.scheduleTaskExecutor = scheduleTaskExecutor;
    }

    @Override
    protected void runTask(List<Long> taskIds) throws Exception {
        scheduleTaskExecutor.execute(taskIds);
        super.threadSleep();
    }
}
