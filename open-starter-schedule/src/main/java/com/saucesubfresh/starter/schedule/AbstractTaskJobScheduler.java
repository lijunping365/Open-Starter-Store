package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.cron.CronHelper;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-07-16 11:49
 */
@Slf4j
public abstract class AbstractTaskJobScheduler implements TaskJobScheduler {

    private Thread scheduleThread;
    private volatile boolean scheduleThreadToStop = false;
    private final ScheduleTaskExecutor scheduleTaskExecutor;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;

    public AbstractTaskJobScheduler(ScheduleTaskExecutor scheduleTaskExecutor,
                                    ScheduleTaskPoolManager scheduleTaskPoolManager,
                                    ScheduleTaskQueueManager scheduleTaskQueueManager) {
        this.scheduleTaskExecutor = scheduleTaskExecutor;
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
    }

    @Override
    public void start() {
        scheduleThread = new Thread(()->{
            while (!scheduleThreadToStop) {
                this.run();
            }
            log.info("scheduleThread stop");
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("scheduleThread");
        scheduleThread.start();
        log.info("scheduleThread start success");
    }

    @Override
    public void stop() {
        scheduleThreadToStop = true;
        stopThread(scheduleThread);
        log.info("scheduleThread stop success");
    }

    /**
     * 从队列中获取当前秒数的任务列表去执行
     */
    protected void takeTask(){
        List<Long> taskIds = scheduleTaskQueueManager.take();
        if (CollectionUtils.isEmpty(taskIds)){
            return;
        }
        try {
            scheduleTaskExecutor.execute(taskIds);
        }catch (Exception e){
            if (!scheduleThreadToStop) {
                log.error("Execute task error:{}", e.getMessage(), e);
            }
        }
        refreshNextTime(taskIds);
    }

    /**
     * 刷新下次执行时间
     *
     * @param taskIds
     */
    protected void refreshNextTime(List<Long> taskIds) {
        for (Long taskId : taskIds) {
            ScheduleTask task = scheduleTaskPoolManager.get(taskId);
            if (Objects.isNull(task)){
                continue;
            }
            try {
                long nextTime = CronHelper.getNextTime(task.getCronExpression());
                scheduleTaskQueueManager.put(taskId, nextTime);
            }catch (Exception e){
                if (!scheduleThreadToStop) {
                    log.error("Refresh task error:{}", e.getMessage(), e);
                }
            }
        }
    }

    protected void threadSleep(long timeout){
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            if (!scheduleThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 停止线程
     * @param thread 要停止的线程
     */
    private void stopThread(Thread thread){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        if (thread.getState() == Thread.State.TERMINATED){
            return;
        }

        // interrupt and wait
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected abstract void run();
}
