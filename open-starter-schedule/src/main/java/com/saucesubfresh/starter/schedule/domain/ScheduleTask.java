package com.saucesubfresh.starter.schedule.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/1/20
 */
@Data
public class ScheduleTask implements Serializable {
    private static final long serialVersionUID = -1936628567018899417L;
    /**
     * 任务唯一 id
     */
    private Long taskId;

    /**
     * 调度任务名称
     */
    private String scheduleName;

    /**
     * cron 表达式
     */
    private String cronExpression;
}
