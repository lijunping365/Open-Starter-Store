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
package com.openbytecode.starter.cache.executor;

import com.openbytecode.starter.cache.core.ClusterCache;
import com.openbytecode.starter.cache.manager.CacheManager;

/**
 * @author lijunping
 */
public abstract class AbstractCacheExecutor implements CacheExecutor {

    private final CacheManager cacheManager;

    protected AbstractCacheExecutor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }
}
