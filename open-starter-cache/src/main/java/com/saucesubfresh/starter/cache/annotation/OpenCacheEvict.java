package com.saucesubfresh.starter.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 使用该注解标志的方法，会清空指定的缓存。一般用在更新或者删除方法上
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenCacheEvict {

    /**
     * 缓存名称
     * @return
     */
    @AliasFor("cacheName")
    String value();

    /**
     * 缓存名称
     * @return
     */
    @AliasFor("value")
    String cacheName();

    /**
     * 缓存 key， 如果未指定则会使用 KeyGenerator 的默认实现去生成
     * @return
     */
    String cacheKey() default "";
}
