package com.saucesubfresh.starter.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/5/20
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cache")
public class CacheProperties {

    private String namespace;
}
