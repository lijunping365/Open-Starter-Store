/*
 * Copyright (c) 2013-2021 Nikita Koksharov
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
package com.openbytecode.starter.cache.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;

/**
 * This design is learning from {@link org.redisson.config.Config} which is in Redisson.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CacheConfig {

    /**
     * 键值条目的存活时间，以秒为单位。
     */
    private long ttl;

    /**
     * 缓存容量
     */
    private int maxSize;
    /**
     * 是否存储空值
     */
    private boolean allowNullValues;
    /**
     * Read config objects stored in JSON format from <code>String</code>
     *
     * @param content of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(String content) throws IOException {
        return new CacheConfigSupport().fromJSON(content);
    }

    /**
     * Read config objects stored in JSON format from <code>InputStream</code>
     *
     * @param inputStream of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(InputStream inputStream) throws IOException {
        return new CacheConfigSupport().fromJSON(inputStream);
    }

    /**
     * Read config objects stored in JSON format from <code>File</code>
     *
     * @param file of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(File file) throws IOException {
        return new CacheConfigSupport().fromJSON(file);
    }

    /**
     * Read config objects stored in JSON format from <code>URL</code>
     *
     * @param url of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(URL url) throws IOException {
        return new CacheConfigSupport().fromJSON(url);
    }

    /**
     * Read config objects stored in JSON format from <code>Reader</code>
     *
     * @param reader of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(Reader reader) throws IOException {
        return new CacheConfigSupport().fromJSON(reader);
    }

    /**
     * Convert current configuration to JSON format
     *
     * @param config object
     * @return json string
     * @throws IOException error
     */
    public static String toJSON(Map<String, ? extends CacheConfig> config) throws IOException {
        return new CacheConfigSupport().toJSON(config);
    }

    /**
     * Read config objects stored in YAML format from <code>String</code>
     *
     * @param content of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(String content) throws IOException {
        return new CacheConfigSupport().fromYAML(content);
    }

    /**
     * Read config objects stored in YAML format from <code>InputStream</code>
     *
     * @param inputStream of config
     * @return config
     * @throws IOException  error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(InputStream inputStream) throws IOException {
        return new CacheConfigSupport().fromYAML(inputStream);
    }

    /**
     * Read config objects stored in YAML format from <code>File</code>
     *
     * @param file of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(File file) throws IOException {
        return new CacheConfigSupport().fromYAML(file);
    }

    /**
     * Read config objects stored in YAML format from <code>URL</code>
     *
     * @param url of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(URL url) throws IOException {
        return new CacheConfigSupport().fromYAML(url);
    }

    /**
     * Read config objects stored in YAML format from <code>Reader</code>
     *
     * @param reader of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(Reader reader) throws IOException {
        return new CacheConfigSupport().fromYAML(reader);
    }

    /**
     * Convert current configuration to YAML format
     *
     * @param config map
     * @return yaml string
     * @throws IOException error
     */
    public static String toYAML(Map<String, ? extends CacheConfig> config) throws IOException {
        return new CacheConfigSupport().toYAML(config);
    }

}
