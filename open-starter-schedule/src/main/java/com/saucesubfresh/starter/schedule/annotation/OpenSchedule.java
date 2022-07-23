package com.saucesubfresh.starter.schedule.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenSchedule {

    /**
     * 调度任务名称
     * @return
     */
    @AliasFor("scheduleName")
    String value() default "";

    /**
     * 调度任务名称
     * @return
     */
    @AliasFor("value")
    String scheduleName() default "";

    /**
     * 调度任务 id
     * @return
     */
    long taskId() default 0L;

    /**
     * cron 表达式
     * @return
     */
    String cron() default "";
}
