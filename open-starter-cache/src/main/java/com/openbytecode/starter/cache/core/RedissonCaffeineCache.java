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
package com.openbytecode.starter.cache.core;

import com.openbytecode.starter.cache.factory.CacheConfig;
import com.openbytecode.starter.cache.stats.ConcurrentStatsCounter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Slf4j
public class RedissonCaffeineCache extends AbstractClusterCache{

    /**
     * RLocalCachedMap 自带本地和远程缓存
     */
    private final RLocalCachedMap<Object, Object> map;

    public RedissonCaffeineCache(String cacheName,
                                 String namespace,
                                 CacheConfig cacheConfig,
                                 RedissonClient redissonClient) {
        super(cacheConfig, new ConcurrentStatsCounter());
        String cacheHashKey = super.generate(namespace, cacheName);
        LocalCachedMapOptions<Object, Object> options = LocalCachedMapOptions.defaults();
        options.cacheProvider(LocalCachedMapOptions.CacheProvider.CAFFEINE);
        options.cacheSize(cacheConfig.getMaxSize());
        options.timeToLive(cacheConfig.getTtl(), TimeUnit.SECONDS);
        this.map = redissonClient.getLocalCachedMap(cacheHashKey, options);
    }

    @Override
    public void preloadCache(int count) {
        map.preloadCache(count);
    }

    @Override
    public Object get(Object key) {
        Object value = map.get(key);
        value = toValueWrapper(value);
        this.afterRead(value);
        return value;
    }

    @Override
    public Object put(Object key, Object value) {
        value = toStoreValue(value);
        if (Objects.nonNull(value)){
            map.fastPut(key, value);
            this.afterPut();
        }
        return value;
    }

    @Override
    public void evict(Object key) {
        map.fastRemove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public long getCacheKeyCount() {
        return map.size();
    }

    @Override
    public Set<Object> getCacheKeySet(String pattern, int count) {
        return map.keySet(pattern,count);
    }
}
