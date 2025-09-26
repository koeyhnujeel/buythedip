package com.zunza.buythedip.common.annotation.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.common.annotation.CacheableWithDistributedLock;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheableWithDistributedLockAspect {
	private static final String LOCK_KEY_PREFIX = "LOCK";
	private final RedissonClient redissonClient;
	private final CacheManager cacheManager;
	private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	private final ExpressionParser expressionParser = new SpelExpressionParser();

	@Around("@annotation(com.zunza.buythedip.common.annotation.CacheableWithDistributedLock)")
	public Object handleCacheableWithDistributedLock(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		CacheableWithDistributedLock annotation = method.getAnnotation(CacheableWithDistributedLock.class);

		String cacheKey = generateCacheKey(annotation.key(), method, joinPoint.getArgs());
		String cacheName = annotation.value()[0];
		Cache cache = cacheManager.getCache(cacheName);

		Cache.ValueWrapper cachedValue = cache.get(cacheKey);
		if (cachedValue != null) {
			return cachedValue.get();
		}

		String lockKey = LOCK_KEY_PREFIX + cacheName + "::" + cacheKey;
		RLock lock = redissonClient.getLock(lockKey);
		boolean isLocked = false;

		try {
			isLocked = lock.tryLock(annotation.maxWaitTime(), annotation.lockExpireTime(), annotation.timeUnit());
			if (!isLocked) {
				throw new RuntimeException("분산 락을 획득하지 못했습니다.");
			}

			// double check
			cachedValue = cache.get(cacheKey);
			if (cachedValue != null) {
				return cachedValue.get();
			}

			Object result = joinPoint.proceed();

			if (result != null) {
				cache.put(cacheKey, result);
			}
			return result;
		} finally {
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	private String generateCacheKey(
		String keyExpression,
		Method method,
		Object[] args
	) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		Expression spelExpression = expressionParser.parseExpression(keyExpression);

		String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
		for (int i = 0; i < args.length; i++) {
			context.setVariable(paramNames[i], args[i]);
		}

		return spelExpression.getValue(context, String.class);
	}
}
