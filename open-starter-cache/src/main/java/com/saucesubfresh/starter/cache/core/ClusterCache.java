package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.stats.CacheStats;

/**
 * @author lijunping on 2022/5/24
 */
public interface ClusterCache{

    /**
     * 二级缓存数据加载到一级缓存
     */
    void preloadCache();

    /**
     * 获取统计数据
     * @return
     */
    CacheStats getStats();
    /**
     * 获取缓存
     * @param key
     * @return
     */
    Object get(Object key);

    /**
     * 添加缓存
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     * 根据 key 清除
     * @param key
     */
    void evict(Object key);

    /**
     * 清空该 cacheName 下的缓存
     */
    void clear();
}
