package com.zunza.buythedip.common.annotation.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.common.annotation.CacheableWithDistributedLock;
import com.zunza.buythedip.infrastructure.redis.service.RedisDistributedLock;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheableWithDistributedLockAspect {
	private final RedisDistributedLock distributedLock;
	private final CacheManager cacheManager;
	private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	private final ExpressionParser expressionParser = new SpelExpressionParser();

	@Around("@annotation(cacheableWithDistributedLock)")
	public Object handleCacheableWithDistributedLock(
		ProceedingJoinPoint joinPoint,
		CacheableWithDistributedLock cacheableWithDistributedLock
	) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		String cacheName = determineCacheName(cacheableWithDistributedLock, method);
		String cacheKey = calculateCacheKey(cacheableWithDistributedLock, method, args);

		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new IllegalArgumentException("캐시가 존재하지 않습니다. [cache name]: " + cacheName);
		}

		Cache.ValueWrapper cachedValue = cache.get(cacheKey);
		if (cachedValue != null) {
			return cachedValue.get();
		}

		return executeWithDistributedLock(joinPoint, cache, cacheKey, cacheableWithDistributedLock);
	}

	private String determineCacheName(
		CacheableWithDistributedLock annotation,
		Method method
	) {
		if (annotation.value().length > 0) {
			return annotation.value()[0];
		}

		return method.getDeclaringClass().getSimpleName() + "." + method.getName();
	}

	private String calculateCacheKey(
		CacheableWithDistributedLock annotation,
		Method method,
		Object[] args
	) {
		if (!annotation.key().isEmpty()) {
			return evaluateSpelExpression(annotation.key(), method, args);
		}

		StringBuilder keyBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) keyBuilder.append(":");
			keyBuilder.append(args[i] != null ? args[i].toString() : "null");
		}
		return keyBuilder.toString();
	}

	private String evaluateSpelExpression(
		String expression,
		Method method,
		Object[] args
	) {
		Expression spelExpression = expressionParser.parseExpression(expression);
		EvaluationContext context = new StandardEvaluationContext();

		String[] paramNames = parameterNameDiscoverer.getParameterNames(method);

		for (int i = 0; i < args.length; i++) {
			context.setVariable(paramNames[i], args[i]);
		}

		return spelExpression.getValue(context, String.class);
	}

	private Object executeWithDistributedLock(
		ProceedingJoinPoint joinPoint,
		Cache cache,
		String cacheKey,
		CacheableWithDistributedLock annotation
	) throws Throwable {
		String lockKey = "lock:" + cache.getName() + ":" + cacheKey;

		RedisDistributedLock.LockInfo lockInfo = distributedLock.tryLockWithRetry(
			lockKey,
			annotation.lockExpireTime(),
			annotation.maxWaitTime(),
			annotation.retryInterval()
		);

		if (lockInfo != null) {
			try {
				Cache.ValueWrapper cachedValue = cache.get(cacheKey);
				if (cachedValue != null) {
					return cachedValue.get();
				}

				Object result = joinPoint.proceed();
				cache.put(cacheKey, result);

				return result;

			} finally {
				distributedLock.unlock(lockInfo);
			}
		} else {
			if (annotation.allowFallback()) {
				return joinPoint.proceed();
			} else {
				throw new RuntimeException("분산 락을 획득하지 못했습니다. [cache key]: " + cacheKey);
			}
		}
	}
}
