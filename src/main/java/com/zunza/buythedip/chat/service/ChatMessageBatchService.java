package com.zunza.buythedip.chat.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.chat.entity.ChatMessage;
import com.zunza.buythedip.infrastructure.redis.RedisStreamService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageBatchService {

	private final MongoTemplate mongoTemplate;
	private final RedisStreamService redisStreamService;

	private static final String STREAM_KEY = "chat:stream";
	private static final String GROUP_NAME = "chat-group";
	private static final String CONSUMER_NAME = "server-" + UUID.randomUUID();

	@PostConstruct
	public void init() {
		try {
			redisStreamService.createGroup(STREAM_KEY, GROUP_NAME);
		} catch (Exception e) {
			log.info("Consumer group '{}' already exists.", GROUP_NAME);
		}
	}

	@Scheduled(fixedRate = 10000)
	public void processChatMessage() {
		StreamReadOptions readOptions = StreamReadOptions.empty()
			.count(50)
			.block(Duration.ofSeconds(2));

		List<MapRecord<String, Object, Object>> messages = redisStreamService.read(GROUP_NAME, CONSUMER_NAME, readOptions, STREAM_KEY);

		if (messages == null || messages.isEmpty()) {
			return;
		}

		List<ChatMessage> chatMessages = convertToDocuments(messages);
		mongoTemplate.insertAll(chatMessages);

		for (MapRecord<String, Object, Object> message : messages) {
			redisStreamService.ack(STREAM_KEY, GROUP_NAME, message.getId());
		}
	}

	private List<ChatMessage> convertToDocuments(List<MapRecord<String, Object, Object>> messages) {
		return messages.stream()
			.map(record -> {
				Map<Object, Object> value = record.getValue();
				return ChatMessage.of(
					(String)value.get("accountId"),
					(String)value.get("sender"),
					(String)value.get("content"),
					Long.parseLong((String)value.get("timestamp")));
			})
			.toList();
	}
}
