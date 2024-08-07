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
package com.openbytecode.starter.crawler.pipeline;

import com.openbytecode.starter.crawler.domain.SpiderRequest;
import com.openbytecode.starter.crawler.domain.SpiderResponse;

/**
 * @author lijunping
 */
public class DefaultCrawlerHandlerContext implements CrawlerHandlerContext{

    volatile DefaultCrawlerHandlerContext next;
    volatile DefaultCrawlerHandlerContext prev;
    private CrawlerHandler handler;
    private final String name;

    public DefaultCrawlerHandlerContext(String name) {
        this.name = name;
    }

    public DefaultCrawlerHandlerContext(String name, CrawlerHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public CrawlerHandler handler() {
        return this.handler;
    }

    @Override
    public void fireCrawlerHandler(SpiderRequest request, SpiderResponse response) {
        invokeCrawlerHandler(next, request, response);
    }

    public static void invokeCrawlerHandler(final DefaultCrawlerHandlerContext next, SpiderRequest request, SpiderResponse response) {
        if (null != next){
            next.invokeCrawlerHandler(request, response);
        }
    }

    private void invokeCrawlerHandler(SpiderRequest request, SpiderResponse response) {
        if (null != handler){
            handler.handler(this, request, response);
        } else {
            fireCrawlerHandler(request, response);
        }
    }
}
