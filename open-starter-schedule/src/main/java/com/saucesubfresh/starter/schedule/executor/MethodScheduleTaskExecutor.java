package com.saucesubfresh.starter.schedule.executor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lijunping on 2022/1/20
 */
public class MethodScheduleTaskExecutor implements ScheduleTaskExecutor{

    private final Object target;
    private final Method method;

    public MethodScheduleTaskExecutor(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void execute(List<Long> taskList) throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            method.invoke(target, taskList);
        } else {
            method.invoke(target);
        }
    }
}
