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
package com.saucesubfresh.starter.alarm.config;

import com.saucesubfresh.starter.alarm.properties.AlarmProperties;
import com.saucesubfresh.starter.alarm.provider.dingtalk.DingDingRobotAlarmExecutor;
import com.saucesubfresh.starter.alarm.provider.email.EmailAlarmExecutor;
import com.saucesubfresh.starter.alarm.provider.feishu.FeiShuRobotAlarmExecutor;
import com.saucesubfresh.starter.alarm.provider.wechat.WeChatRobotAlarmExecutor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author lijunping
 */
@Configuration
@EnableConfigurationProperties(AlarmProperties.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class AlarmAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JavaMailSender.class)
    public EmailAlarmExecutor mailAlarmExecutor(JavaMailSender mailSender){
        return new EmailAlarmExecutor(mailSender);
    }

    @Bean
    @ConditionalOnMissingBean
    public DingDingRobotAlarmExecutor dingDingAlarmExecutor(AlarmProperties alarmProperties){
        return new DingDingRobotAlarmExecutor(alarmProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatRobotAlarmExecutor weChatAlarmExecutor(AlarmProperties alarmProperties){
        return new WeChatRobotAlarmExecutor(alarmProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public FeiShuRobotAlarmExecutor feiShuRobotAlarmExecutor(AlarmProperties alarmProperties){
        return new FeiShuRobotAlarmExecutor(alarmProperties);
    }
}
