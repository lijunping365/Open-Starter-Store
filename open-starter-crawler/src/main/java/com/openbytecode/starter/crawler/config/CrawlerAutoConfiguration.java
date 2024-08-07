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
package com.openbytecode.starter.crawler.config;

import com.openbytecode.starter.crawler.generator.DefaultKeyGenerator;
import com.openbytecode.starter.crawler.generator.KeyGenerator;
import com.openbytecode.starter.crawler.pipeline.CrawlerPipeline;
import com.openbytecode.starter.crawler.pipeline.DefaultCrawlerPipeline;
import com.openbytecode.starter.crawler.properties.CrawlerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CrawlerProperties.class)
public class CrawlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CrawlerPipeline crawlerPipeline(){
        return new DefaultCrawlerPipeline();
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new DefaultKeyGenerator();
    }
}
