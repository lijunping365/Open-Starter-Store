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
package com.saucesubfresh.starter.logger;

import com.saucesubfresh.starter.logger.properties.LoggerProperties;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class DefaultLifeCycle implements LifeCycle{

    private static final String ROOT = "/";

    private final LoggerEvict loggerEvict;
    private final LoggerProperties properties;

    public DefaultLifeCycle(LoggerProperties properties, LoggerEvict loggerEvict) {
        this.properties = properties;
        this.loggerEvict = loggerEvict;
    }

    @Override
    public void start() {
        String logBasePath = ROOT;
        String logPath = properties.getPath();
        if (StringUtils.isNotBlank(logPath)) {
            logBasePath = logPath;
        }
        // mk base dir
        File logPathDir = new File(logBasePath);
        if (!logPathDir.exists()) {
            logPathDir.mkdirs();
        }
        logBasePath = logPathDir.getPath();
        LoggerContext.setLogBasePath(logBasePath);
    }

    @Override
    public void stop() {
        loggerEvict.evict();
    }

}
