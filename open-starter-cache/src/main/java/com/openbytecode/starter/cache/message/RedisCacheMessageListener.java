/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.cache.message;

import com.openbytecode.starter.cache.executor.CacheExecutor;
import com.openbytecode.starter.cache.properties.CacheProperties;
import com.openbytecode.starter.cache.handler.CacheListenerErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lijunping
 */
@Slf4j
public class RedisCacheMessageListener extends AbstractCacheMessageListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageListener(CacheExecutor cacheExecutor,
                                     CacheProperties properties,
                                     CacheListenerErrorHandler errorHandler,
                                     RedisTemplate<String, Object> redisTemplate) {
        super(cacheExecutor, properties, errorHandler);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        super.onMessage(cacheMessage);
    }
}
