package com.saucesubfresh.starter.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lijunping on 2022/5/20
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cache")
public class CacheProperties {
    /**
     * 缓存实例 id
     */
    private Long instanceId = ThreadLocalRandom.current().nextLong();
    /**
     * 配置文件
     */
    private String configLocation = "classpath:/open-cache-config.yaml";
    /**
     * 命名空间，相当于应用名称，应当改为自己的
     */
    private String namespace = "open-cache";
    /**
     * 是否开启自动缓存指标信息上报
     */
    private boolean enableMetricsReport = false;
    /**
     * 自动上报周期，默认 60，单位秒
     */
    private long metricsReportCycle = 60;
    /**
     * 键值输入的最大空闲时间(毫秒)。
     */
    private long maxIdleTime = 720000;
    /**
     * 缓存容量（缓存数量最大值）
     */
    private int maxSize = 100;
    /**
     * 键值条目的存活时间，以毫秒为单位。
     */
    private long ttl = 30000;

}
