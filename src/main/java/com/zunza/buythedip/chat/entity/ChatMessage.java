package com.zunza.buythedip.chat.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Document(collection = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

	@Id
	private String id;
	private String accountId;
	private String sender;
	private String content;
	private long timestamp;

	@Builder
	private ChatMessage(String accountId, String sender, String content, long timestamp) {
		this.accountId = accountId;
		this.sender = sender;
		this.content = content;
		this.timestamp = timestamp;
	}

	public static ChatMessage of(String accountId, String sender, String content, long timestamp) {
		return ChatMessage.builder()
			.accountId(accountId)
			.sender(sender)
			.content(content)
			.timestamp(timestamp)
			.build();
	}
}
