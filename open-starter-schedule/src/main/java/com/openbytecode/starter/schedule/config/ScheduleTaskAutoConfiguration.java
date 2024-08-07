/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.schedule.config;


import com.openbytecode.starter.schedule.OpenJobPrepareScheduler;
import com.openbytecode.starter.schedule.OpenJobScheduler;
import com.openbytecode.starter.schedule.OpenJobTriggerScheduler;
import com.openbytecode.starter.schedule.annotation.EnableOpenScheduler;
import com.openbytecode.starter.schedule.executor.DefaultScheduleTaskExecutor;
import com.openbytecode.starter.schedule.executor.ScheduleTaskExecutor;
import com.openbytecode.starter.schedule.initializer.DefaultScheduleTaskInitializer;
import com.openbytecode.starter.schedule.initializer.ScheduleTaskInitializer;
import com.openbytecode.starter.schedule.properties.ScheduleProperties;
import com.openbytecode.starter.schedule.service.DefaultScheduleTaskService;
import com.openbytecode.starter.schedule.service.ScheduleTaskService;
import com.openbytecode.starter.schedule.wheel.HashedTimeWheel;
import com.openbytecode.starter.schedule.wheel.TimeWheel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 定时任务执行器配置类
 *
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ScheduleProperties.class)
@ConditionalOnBean(annotation = {EnableOpenScheduler.class})
public class ScheduleTaskAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskService scheduleTaskService(){
    return new DefaultScheduleTaskService();
  }

  @Bean
  @ConditionalOnMissingBean
  public TimeWheel timeWheel(ScheduleProperties scheduleProperties){
    return new HashedTimeWheel(scheduleProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutor scheduleTaskExecutor(){
    return new DefaultScheduleTaskExecutor();
  }

  @Bean
  public OpenJobScheduler prepareScheduler(TimeWheel timeWheel, ScheduleTaskService scheduleTaskService){
    return new OpenJobPrepareScheduler(timeWheel, scheduleTaskService);
  }

  @Bean
  public OpenJobScheduler triggerJobScheduler(TimeWheel timeWheel, ScheduleTaskExecutor executor){
    return new OpenJobTriggerScheduler(timeWheel, executor);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskInitializer scheduleTaskInitializer(List<OpenJobScheduler> openJobScheduler){
    return new DefaultScheduleTaskInitializer(openJobScheduler);
  }
}
