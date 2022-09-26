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
package com.saucesubfresh.starter.oauth.properties;

import com.saucesubfresh.starter.oauth.properties.social.SocialProperties;
import com.saucesubfresh.starter.oauth.properties.token.TokenProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 安全相关配置
 *
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.oauth")
public class OAuthProperties {

  /**
   * 认证服务器相关的配置
   */
  @NestedConfigurationProperty
  private TokenProperties token = new TokenProperties();

  /**
   * spring-social相关的配置
   */
  @NestedConfigurationProperty
  private SocialProperties social = new SocialProperties();

}
