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
package com.openbytecode.starter.cache.manager;

import com.openbytecode.starter.cache.core.ClusterCache;
import com.openbytecode.starter.cache.core.RedissonCaffeineCache;
import com.openbytecode.starter.cache.properties.CacheProperties;
import com.openbytecode.starter.cache.factory.CacheConfig;
import com.openbytecode.starter.cache.factory.ConfigFactory;
import org.redisson.api.RedissonClient;

/**
 * @author lijunping
 */
public class RedissonCaffeineCacheManager extends AbstractCacheManager {
    private final RedissonClient client;
    private final CacheProperties properties;

    public RedissonCaffeineCacheManager(CacheProperties properties,
                                        ConfigFactory configFactory,
                                        RedissonClient redissonClient) {
        super(configFactory);
        this.properties = properties;
        this.client = redissonClient;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        return new RedissonCaffeineCache(cacheName, namespace, cacheConfig, client);
    }
}
