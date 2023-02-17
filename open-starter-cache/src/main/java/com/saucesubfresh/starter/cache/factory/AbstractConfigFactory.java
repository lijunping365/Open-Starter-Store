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
package com.saucesubfresh.starter.cache.factory;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
*  1. 项目启动会自动加载缓存配置文件并放到 configMap 中
*  2. 在 configMap 中未找到配置文件时会创建默认的 CacheConfig 并返回
 * </p>
 *
 * @author lijunping
 */
@Slf4j
public abstract class AbstractConfigFactory implements ConfigFactory, InitializingBean{

    protected final CacheProperties properties;

    private final ConcurrentMap<String, CacheConfig> configMap = new ConcurrentHashMap<>(16);

    public AbstractConfigFactory(CacheProperties properties) {
        this.properties = properties;
    }

    @Override
    public CacheConfig create(String cacheName) {
        CacheConfig cacheConfig = configMap.get(cacheName);
        if (Objects.nonNull(cacheConfig)){
            return cacheConfig;
        }
        CacheConfig defaultConfig = createDefault();
        configMap.put(cacheName, defaultConfig);
        return defaultConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ? extends CacheConfig> config = this.loadConfig();
        if (!CollectionUtils.isEmpty(config)){
            configMap.putAll(config);
        }
    }

    private CacheConfig createDefault(){
        return CacheConfig.builder()
                .maxSize(properties.getMaxSize())
                .ttl(properties.getTtl())
                .build();
    }

    protected abstract Map<String, ? extends CacheConfig> loadConfig();

}
