package com.zunza.buythedip.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.chat.dto.ChatMessageDto;
import com.zunza.buythedip.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat/room/public")
	public void sendMessage(
		ChatMessageDto chatMessageDto
	) {
		chatService.sendMessage(chatMessageDto);
	}
}
