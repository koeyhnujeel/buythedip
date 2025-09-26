package com.zunza.buythedip.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableWithDistributedLock {

	String[] value() default {};

	String key() default "";

	long maxWaitTime() default 5;

	long lockExpireTime() default 10;

	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
