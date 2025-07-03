package com.zunza.buythedip.config;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.zunza.buythedip.constant.CacheType;

@EnableCaching
@Configuration
public class CacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		Map<String, RedisCacheConfiguration> cacheConfigurations = Stream.of(CacheType.values())
			.collect(Collectors.toMap(
				CacheType::getCacheName,
				cacheType -> defaultConfig.entryTtl(cacheType.getExpiredAfterWrite())
			));

		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(defaultConfig.entryTtl(Duration.ofMinutes(1)))
			.withInitialCacheConfigurations(cacheConfigurations)
			.build();
	}
}
