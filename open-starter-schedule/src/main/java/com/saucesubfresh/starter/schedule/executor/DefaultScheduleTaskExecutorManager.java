package com.saucesubfresh.starter.schedule.executor;

import com.saucesubfresh.starter.schedule.annotation.OpenSchedule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 李俊平
 * @Date: 2022-07-23 13:46
 */
@Slf4j
public class DefaultScheduleTaskExecutorManager implements ScheduleTaskExecutorManager, ApplicationContextAware, SmartInitializingSingleton {

    private final ConcurrentMap<String, ScheduleTaskExecutor> handlerMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public ScheduleTaskExecutor getTaskExecutor(String scheduleName) {
        return handlerMap.get(scheduleName);
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.initClazzJobHandler();
        this.initMethodJobHandler();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void initClazzJobHandler(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(OpenSchedule.class);
        if (ObjectUtils.isEmpty(beans)){
            log.warn("No ScheduleTaskExecutor instance is defined.");
        }else {
            beans.forEach((k,v)->{
                OpenSchedule annotation = v.getClass().getAnnotation(OpenSchedule.class);
                handlerMap.put(annotation.value(), (ScheduleTaskExecutor) v);
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
        if (handlerMap.get(name) != null) {
            throw new RuntimeException("OpenSchedule executor[" + name + "] naming conflicts.");
        }
        executeMethod.setAccessible(true);
        handlerMap.put(name, new MethodScheduleTaskExecutor(bean, executeMethod));
    }
}
