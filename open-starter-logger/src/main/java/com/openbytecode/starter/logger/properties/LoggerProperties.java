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
package com.openbytecode.starter.logger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * logger properties
 *
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.openbytecode.logger")
public class LoggerProperties {

    /**
     * The base path for writing log file
     */
    private String path;

    /**
     * The max
     */
    private Integer maxFileSize = Integer.MAX_VALUE;

    /**
     * The days of log retention, over this value will be deleted
     */
    private Integer logRetentionDays = 3;

}