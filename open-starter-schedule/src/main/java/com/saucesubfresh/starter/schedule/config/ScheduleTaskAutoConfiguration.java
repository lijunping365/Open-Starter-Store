package com.saucesubfresh.starter.schedule.config;


import com.saucesubfresh.starter.schedule.DefaultTaskJobScheduler;
import com.saucesubfresh.starter.schedule.TaskJobScheduler;
import com.saucesubfresh.starter.schedule.annotation.EnableOpenSchedule;
import com.saucesubfresh.starter.schedule.manager.DefaultScheduleTaskExecutorManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskExecutorManager;
import com.saucesubfresh.starter.schedule.initializer.DefaultScheduleTaskInitializer;
import com.saucesubfresh.starter.schedule.initializer.ScheduleTaskInitializer;
import com.saucesubfresh.starter.schedule.loader.DefaultScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.loader.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.manager.HashedWheelScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.manager.LocalScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 定时任务执行器配置类
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ScheduleProperties.class)
@ConditionalOnBean(annotation = {EnableOpenSchedule.class})
public class ScheduleTaskAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskLoader scheduleTaskLoader(){
    return new DefaultScheduleTaskLoader();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskPoolManager scheduleTaskManager(){
    return new LocalScheduleTaskPoolManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskQueueManager scheduleTaskQueueManager(ScheduleProperties scheduleProperties){
    return new HashedWheelScheduleTaskQueueManager(scheduleProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutorManager scheduleTaskExecutorManager(){
    return new DefaultScheduleTaskExecutorManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskJobScheduler taskJobScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager,
                                           ScheduleTaskQueueManager scheduleTaskQueueManager,
                                           ScheduleTaskExecutorManager scheduleTaskExecutorManager){
    return new DefaultTaskJobScheduler(scheduleTaskPoolManager, scheduleTaskQueueManager, scheduleTaskExecutorManager);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskInitializer taskInitializer(TaskJobScheduler taskJobScheduler,
                                                 ScheduleTaskLoader scheduleTaskLoader,
                                                 ScheduleTaskPoolManager scheduleTaskPoolManager,
                                                 ScheduleTaskQueueManager scheduleTaskQueueManager,
                                                 ScheduleTaskExecutorManager scheduleTaskExecutorManager){
    return new DefaultScheduleTaskInitializer(taskJobScheduler, scheduleTaskLoader, scheduleTaskPoolManager, scheduleTaskQueueManager, scheduleTaskExecutorManager);
  }
}
