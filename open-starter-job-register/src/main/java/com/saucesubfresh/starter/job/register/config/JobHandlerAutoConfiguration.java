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
package com.saucesubfresh.starter.job.register.config;

import com.saucesubfresh.starter.job.register.core.DefaultJobHandlerHolder;
import com.saucesubfresh.starter.job.register.core.DefaultJobHandlerRegister;
import com.saucesubfresh.starter.job.register.core.JobHandlerHolder;
import com.saucesubfresh.starter.job.register.core.JobHandlerRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping on 2022/6/13
 */
@Configuration
public class JobHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JobHandlerHolder jobHandlerHolder(){
        return new DefaultJobHandlerHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public JobHandlerRegister jobHandlerRegister(JobHandlerHolder jobHandlerHolder){
        return new DefaultJobHandlerRegister(jobHandlerHolder);
    }
}
