package com.saucesubfresh.starter.schedule.initializer;

import com.saucesubfresh.starter.schedule.TaskJobScheduler;
import com.saucesubfresh.starter.schedule.annotation.OpenSchedule;
import com.saucesubfresh.starter.schedule.cron.CronHelper;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.executor.MethodScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.loader.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskExecutorManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author: 李俊平
 * @Date: 2022-07-08 23:17
 */
@Slf4j
public class DefaultScheduleTaskInitializer implements ScheduleTaskInitializer, ApplicationContextAware, InitializingBean, DisposableBean {

    private ApplicationContext applicationContext;
    private final TaskJobScheduler taskJobScheduler;
    private final ScheduleTaskLoader scheduleTaskLoader;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;
    private final ScheduleTaskExecutorManager scheduleTaskExecutorManager;

    public DefaultScheduleTaskInitializer(TaskJobScheduler taskJobScheduler,
                                          ScheduleTaskLoader scheduleTaskLoader,
                                          ScheduleTaskPoolManager scheduleTaskPoolManager,
                                          ScheduleTaskQueueManager scheduleTaskQueueManager,
                                          ScheduleTaskExecutorManager scheduleTaskExecutorManager) {
        this.taskJobScheduler = taskJobScheduler;
        this.scheduleTaskLoader = scheduleTaskLoader;
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
        this.scheduleTaskExecutorManager = scheduleTaskExecutorManager;
    }

    @Override
    public void init() {
        List<ScheduleTask> scheduleTasks = new ArrayList<>();
        scheduleTasks.addAll(initClazzJobHandler());
        scheduleTasks.addAll(initMethodJobHandler());
        scheduleTasks.addAll(scheduleTaskLoader.loadScheduleTask());
        if (!CollectionUtils.isEmpty(scheduleTasks)){
            scheduleTaskPoolManager.addAll(scheduleTasks);
            for (ScheduleTask task : scheduleTasks) {
                long nextTime = CronHelper.getNextTime(task.getCronExpression());
                scheduleTaskQueueManager.put(task.getTaskId(), nextTime);
            }
        }
        taskJobScheduler.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            init();
            log.info("Schedule task initialize succeed");
        }catch (Exception e){
            log.error("Schedule task initialize failed, {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        taskJobScheduler.stop();
    }

    private List<ScheduleTask> initClazzJobHandler(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(OpenSchedule.class);
        if (!CollectionUtils.isEmpty(beans)){
            List<ScheduleTask> scheduleTasks = new ArrayList<>();
            beans.forEach((k,v)->{
                OpenSchedule annotation = v.getClass().getAnnotation(OpenSchedule.class);
                String name = annotation.value();
                if (StringUtils.isBlank(name)) {
                    throw new RuntimeException("OpenSchedule class-executor name invalid, for[" + k +"] .");
                }
                if (scheduleTaskExecutorManager.getTaskExecutor(name) != null) {
                    throw new RuntimeException("OpenSchedule executor[" + name + "] naming conflicts.");
                }
                scheduleTaskExecutorManager.put(name, (ScheduleTaskExecutor) v);
                if (!Objects.equals(annotation.taskId(), 0L) && StringUtils.isNotBlank(annotation.cron())){
                    ScheduleTask scheduleTask = buildScheduleTask(name, annotation.taskId(), annotation.cron());
                    scheduleTasks.add(scheduleTask);
                }
            });
            return scheduleTasks;
        }else {
            log.warn("No ScheduleTaskExecutor instance is defined.");
            return Collections.emptyList();
        }
    }

    private List<ScheduleTask> initMethodJobHandler(){
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        List<ScheduleTask> scheduleTasks = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            Map<Method, OpenSchedule> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<OpenSchedule>() {
                            @Override
                            public OpenSchedule inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, OpenSchedule.class);
                            }
                        });
            } catch (Throwable ex) {
                log.error("OpenSchedule resolve error for bean[" + beanDefinitionName + "].", ex);
            }
            if (annotatedMethods == null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, OpenSchedule> methodScheduleExecutorEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodScheduleExecutorEntry.getKey();
                OpenSchedule annotation = methodScheduleExecutorEntry.getValue();
                String name = annotation.value();
                if (StringUtils.isBlank(name)) {
                    throw new RuntimeException("OpenSchedule method-executor name invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
                }
                if (scheduleTaskExecutorManager.getTaskExecutor(name) != null) {
                    throw new RuntimeException("OpenSchedule executor[" + name + "] naming conflicts.");
                }
                ScheduleTaskExecutor executor = buildScheduleExecutor(bean, executeMethod);
                scheduleTaskExecutorManager.put(name, executor);
                if (!Objects.equals(annotation.taskId(), 0L) && StringUtils.isNotBlank(annotation.cron())){
                    ScheduleTask scheduleTask = buildScheduleTask(name, annotation.taskId(), annotation.cron());
                    scheduleTasks.add(scheduleTask);
                }
            }
        }
        return scheduleTasks;
    }

    private ScheduleTaskExecutor buildScheduleExecutor(Object bean, Method executeMethod){
        executeMethod.setAccessible(true);
        return new MethodScheduleTaskExecutor(bean, executeMethod);
    }

    private ScheduleTask buildScheduleTask(String scheduleName, Long taskId, String cron){
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setScheduleName(scheduleName);
        scheduleTask.setTaskId(taskId);
        scheduleTask.setCronExpression(cron);
        return scheduleTask;
    }
}
