package com.saucesubfresh.starter.cache.message;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public interface CacheMessageConsumer {

    void onMessage(CacheMessage message);
}
