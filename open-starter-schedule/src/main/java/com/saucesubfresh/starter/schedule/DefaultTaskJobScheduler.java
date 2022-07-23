package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.manager.ScheduleTaskExecutorManager;
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

    public DefaultTaskJobScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager,
                                   ScheduleTaskQueueManager scheduleTaskQueueManager,
                                   ScheduleTaskExecutorManager scheduleTaskExecutorManager) {
        super(scheduleTaskPoolManager, scheduleTaskQueueManager, scheduleTaskExecutorManager);
    }

    @Override
    protected void runTask(List<Long> taskIds) throws Exception {
        super.executeTask(taskIds);
        super.threadSleep();
    }
}
