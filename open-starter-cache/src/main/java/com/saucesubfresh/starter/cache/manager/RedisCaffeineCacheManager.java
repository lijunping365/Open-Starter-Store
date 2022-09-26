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
package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.RedisCaffeineCache;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lijunping
 */
public class RedisCaffeineCacheManager extends AbstractCacheManager {

    private final CacheProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCaffeineCacheManager(CacheProperties properties,
                                     ConfigFactory configFactory,
                                     RedisTemplate<String, Object> redisTemplate) {
        super(configFactory);
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        return new RedisCaffeineCache(cacheName, namespace, cacheConfig, redisTemplate);
    }
}
