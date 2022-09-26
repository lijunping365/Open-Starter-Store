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
package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.exception.CacheException;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultCacheExecutorErrorHandler implements CacheExecutorErrorHandler {

    @Override
    public void onExecuteError(CacheException cacheException) {
        CacheMessage message = cacheException.getCacheMessage();
        final String cacheName = message.getCacheName();
        CacheCommand command = message.getCommand();
        log.error("缓存操作执行异常 = {}，cacheName = {}, 异常原因 = {}", command.name(), cacheName, message);
    }
}
