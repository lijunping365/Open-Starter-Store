package com.saucesubfresh.starter.schedule.initializer;

import com.saucesubfresh.starter.schedule.annotation.OpenSchedule;
import com.saucesubfresh.starter.schedule.cron.CronHelper;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.executor.MethodScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.loader.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskExecutorManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.TaskJobScheduler;
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
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
    public void initialize() {
        List<ScheduleTask> scheduleTasks = scheduleTaskLoader.loadScheduleTask();
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
            initialize();
            log.info("Schedule task initialize succeed");
        }catch (Exception e){
            log.error("Schedule task initialize failed, {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        taskJobScheduler.stop();
    }

    private void initClazzJobHandler(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(OpenSchedule.class);
        if (ObjectUtils.isEmpty(beans)){
            log.warn("No ScheduleTaskExecutor instance is defined.");
        }else {
            beans.forEach((k,v)->{
                OpenSchedule annotation = v.getClass().getAnnotation(OpenSchedule.class);
                scheduleTaskExecutorManager.put(annotation.value(), (ScheduleTaskExecutor) v);
            });
        }
    }

    private void initMethodJobHandler(){
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
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
                buildScheduleExecutor(annotation, bean, executeMethod);
            }
        }
    }

    private void buildScheduleExecutor(OpenSchedule scheduleExecutor, Object bean, Method executeMethod){
        if (scheduleExecutor == null) {
            return;
        }

        String name = scheduleExecutor.value();
        Class<?> clazz = bean.getClass();
        String methodName = executeMethod.getName();
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("OpenSchedule method-executor name invalid, for[" + clazz + "#" + methodName + "] .");
        }
        if (scheduleTaskExecutorManager.getTaskExecutor(name) != null) {
            throw new RuntimeException("OpenSchedule executor[" + name + "] naming conflicts.");
        }
        executeMethod.setAccessible(true);
        scheduleTaskExecutorManager.put(name, new MethodScheduleTaskExecutor(bean, executeMethod));
    }
}
