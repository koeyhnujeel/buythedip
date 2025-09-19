package com.zunza.buythedip.infrastructure.redis.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.zunza.buythedip.infrastructure.redis.constant.CacheType;

@EnableCaching
@Configuration
public class RedisCacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		Map<String, RedisCacheConfiguration> cacheConfiguration = Arrays.stream(CacheType.values())
			.collect(Collectors.toMap(CacheType::getCacheName, cacheType -> config.entryTtl(cacheType.getTtl())));

		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(config.entryTtl(Duration.ofMinutes(1)))
			.withInitialCacheConfigurations(cacheConfiguration)
			.build();
	}
}
