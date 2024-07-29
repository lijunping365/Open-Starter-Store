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

import com.openbytecode.starter.cache.exception.CacheBroadcastException;
import com.openbytecode.starter.cache.properties.CacheProperties;
import com.openbytecode.starter.cache.handler.CacheProducerErrorHandler;

/**
 * @author lijunping on 2023/1/30
 */
public abstract class AbstractCacheMessageProducer implements CacheMessageProducer{

    private final CacheProperties properties;
    private final CacheProducerErrorHandler errorHandler;

    protected AbstractCacheMessageProducer(CacheProperties properties,
                                           CacheProducerErrorHandler errorHandler) {
        this.properties = properties;
        this.errorHandler = errorHandler;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String instanceId = properties.getInstanceId();
        message.setInstanceId(instanceId);
        try {
            broadcastCacheMessage(message);
        }catch (CacheBroadcastException e){
            errorHandler.onProducerError(e, message);
        }
    }

    protected abstract void broadcastCacheMessage(CacheMessage message);
}