package com.zunza.buythedip.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageDto {
	private String sender;
	private String content;
	private Long timestamp;

	@Builder
	private ChatMessageDto(String sender, String content, Long timestamp) {
		this.sender = sender;
		this.content = content;
		this.timestamp = timestamp;
	}

	public static ChatMessageDto of(String sender, String content, Long timestamp) {
		return ChatMessageDto.builder()
			.sender(sender)
			.content(content)
			.timestamp(timestamp)
			.build();
	}
}
