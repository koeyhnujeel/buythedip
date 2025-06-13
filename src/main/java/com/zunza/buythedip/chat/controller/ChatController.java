package com.zunza.buythedip.chat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.chat.dto.ChatMessageDto;
import com.zunza.buythedip.chat.service.ChatService;
import com.zunza.buythedip.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat/room/public")
	public void sendMessage(
		ChatMessageDto chatMessageDto,
		Principal principal
	) {
		String accountId = getAccountId(principal);
		chatService.sendMessage(accountId, chatMessageDto);
	}

	private String getAccountId(Principal principal) {
		Authentication auth = (Authentication)principal;
		CustomUserDetails details = (CustomUserDetails)auth.getPrincipal();
		return details.getUsername();
	}
}
