package com.lightcode.starter.security.annotation;

import com.lightcode.starter.security.selector.SecurityInterceptorSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用权限控制注解
 *
 * @author: 李俊平
 * @Date: 2020-11-19 12:56
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Import(SecurityInterceptorSelector.class)
public @interface EnableSecurity {
  
}