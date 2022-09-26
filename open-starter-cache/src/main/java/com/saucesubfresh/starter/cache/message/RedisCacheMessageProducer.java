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
package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lijunping
 */
@Slf4j
public class RedisCacheMessageProducer implements CacheMessageProducer {

    private final CacheProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageProducer(CacheProperties properties,
                                     RedisTemplate<String, Object> redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String namespace = properties.getNamespace();
        Long instanceId = properties.getInstanceId();
        message.setInstanceId(instanceId);
        try {
            redisTemplate.convertAndSend(namespace, message);
            log.info("发送缓存同步消息成功");
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
