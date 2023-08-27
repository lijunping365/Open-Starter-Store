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
package com.saucesubfresh.starter.schedule.wheel;

import com.saucesubfresh.starter.schedule.cron.CronHelper;
import com.saucesubfresh.starter.schedule.domain.WheelEntity;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 哈希时间轮实现
 *
 * @author lijunping
 */
public class HashedTimeWheel implements TimeWheel {

    private final long tickDuration;
    private static final Map<Integer, Set<WheelEntity>> timeWheel = new ConcurrentHashMap<>();

    public HashedTimeWheel(ScheduleProperties scheduleProperties){
        long tickDuration = scheduleProperties.getTickDuration();
        if (tickDuration <= 0){
            throw new ScheduleException("The TaskPoolName cannot be empty.");
        }
        this.tickDuration = tickDuration;
    }

    @Override
    public void put(Long taskId, String cron) {
        long nextTime = CronHelper.getNextTime(cron);
        long nowTime = System.currentTimeMillis() / 1000;
        if (nextTime <= nowTime){
            throw new ScheduleException("The nextTime must more than the nowTime");
        }

        long diff = nextTime - nowTime;
        long round = diff / tickDuration;
        int tick = (int) (nextTime % tickDuration);

        Set<WheelEntity> taskSet = timeWheel.getOrDefault(tick, new HashSet<>());
        WheelEntity wheelEntity = new WheelEntity();
        wheelEntity.setRound(round);
        wheelEntity.setTaskId(taskId);
        taskSet.add(wheelEntity);
        timeWheel.put(tick, taskSet);
    }

    @Override
    public List<Long> take(int slot) {
        Set<WheelEntity> entities = timeWheel.get(slot);

        if (CollectionUtils.isEmpty(entities)){
            return Collections.emptyList();
        }

        Set<WheelEntity> tasks = entities.stream().filter(e -> e.getRound() <= 1L).collect(Collectors.toSet());

        entities.removeAll(tasks);

        updateRound(slot, entities);

        return tasks.stream().map(WheelEntity::getTaskId).collect(Collectors.toList());
    }

    private void updateRound(int tick, Set<WheelEntity> entities){
        if (!CollectionUtils.isEmpty(entities)){
            for (WheelEntity entity : entities) {
                entity.setRound(entity.getRound() - 1L);
            }

            timeWheel.put(tick, entities);
        }
    }
}
