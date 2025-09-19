package com.zunza.buythedip.infrastructure.redis.service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;


@Component
public class RedisDistributedLock {
	private final StringRedisTemplate redisTemplate;

	private static final String UNLOCK_SCRIPT =
		"if redis.call('get', KEYS[1]) == ARGV[1] then " +
			"    return redis.call('del', KEYS[1]) " +
			"else " +
			"    return 0 " +
			"end";

	private final DefaultRedisScript<Long> unlockScript;

	public RedisDistributedLock(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.unlockScript = new DefaultRedisScript<>();
		this.unlockScript.setScriptText(UNLOCK_SCRIPT);
		this.unlockScript.setResultType(Long.class);
	}

	public boolean tryLock(
		String lockKey,
		String lockValue,
		long expireTime
	) {
		Boolean result = redisTemplate.opsForValue()
			.setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.SECONDS);
		return Boolean.TRUE.equals(result);
	}

	public LockInfo tryLockWithRetry(
		String lockKey,
		long expireTime,
		long maxWaitTime,
		long retryInterval
	) {
		String lockValue = UUID.randomUUID().toString();
		long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() - startTime < maxWaitTime) {
			if (tryLock(lockKey, lockValue, expireTime)) {
				return new LockInfo(lockKey, lockValue);
			}

			try {
				Thread.sleep(retryInterval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return null;
			}
		}

		return null;
	}

	public boolean unlock(String lockKey, String lockValue) {
		Long result = redisTemplate.execute(unlockScript,
			Collections.singletonList(lockKey), lockValue);
		return Long.valueOf(1).equals(result);
	}

	public boolean unlock(LockInfo lockInfo) {
		if (lockInfo == null) {
			return false;
		}
		return unlock(lockInfo.getLockKey(), lockInfo.getLockValue());
	}

	public static class LockInfo {
		private final String lockKey;
		private final String lockValue;

		public LockInfo(String lockKey, String lockValue) {
			this.lockKey = lockKey;
			this.lockValue = lockValue;
		}

		public String getLockKey() {
			return lockKey;
		}

		public String getLockValue() {
			return lockValue;
		}
	}
}
