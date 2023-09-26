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
package com.saucesubfresh.starter.executor.thread;

import com.saucesubfresh.starter.executor.properties.TaskExecutorProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author lijunping
 */
@Slf4j
public class TaskThreadPoolExecutor extends ThreadPoolExecutor {

    public TaskThreadPoolExecutor(TaskExecutorProperties properties, BlockingQueue<Runnable> workQueue){
        super(properties.getCorePoolSize(), properties.getMaximumPoolSize(), properties.getKeepAliveTime(), TimeUnit.SECONDS, workQueue,
            new NamedThreadFactory(properties.getPrefix()),
            new AbortPolicy());
    }
}
