package com.zunza.buythedip.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableWithDistributedLock {

	String[] value() default {};

	String key() default "";

	long maxWaitTime() default 5000;

	long lockExpireTime() default 30;

	long retryInterval() default 100;

	boolean allowFallback() default true;
}
