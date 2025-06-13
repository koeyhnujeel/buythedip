package com.zunza.buythedip.infrastructure.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisStreamService {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final String STREAM_KEY = "chat:stream";

	public void addToStream(String accountId, ChatMessageDto chatMessageDto) {
		Map<String, String> messageMap = converToMap(accountId, chatMessageDto);
		redisTemplate.opsForStream().add(STREAM_KEY, messageMap);
	}

	private Map<String, String> converToMap(String accountId, ChatMessageDto chatMessageDto) {
		Map<String, String> map = new HashMap<>();
		map.put("accountId", accountId);
		map.put("sender", chatMessageDto.getSender());
		map.put("content", chatMessageDto.getContent());
		map.put("timestamp", String.valueOf(chatMessageDto.getTimestamp()));
		return map;
	}

	public void createGroup(String key, String group) {
		redisTemplate.opsForStream()
			.createGroup(key, ReadOffset.from("0"), group);
	}

	public List<MapRecord<String, Object, Object>> read(String group, String consumer,
		StreamReadOptions readOptions, String key) {

		return redisTemplate.opsForStream().read(
			Consumer.from(group, consumer),
			readOptions,
			StreamOffset.create(key, ReadOffset.lastConsumed())
		);
	}

	public void ack(String key, String group, RecordId id) {
		redisTemplate.opsForStream().acknowledge(key, group, id);
	}
}
