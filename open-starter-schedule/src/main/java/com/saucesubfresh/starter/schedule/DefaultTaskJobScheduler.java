/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author lijunping
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
    protected void runTask(List<Long> taskIds) {
        scheduleTaskExecutor.execute(taskIds);
        super.threadSleep();
    }
}
