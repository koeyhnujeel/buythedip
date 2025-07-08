package com.zunza.buythedip.cryptocurrency.service.kline;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.SubDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class KlineStreamManager {

	private final ObjectMapper objectMapper;
	private WebSocketSession session;

	private static final String SYMBOL_SUFFIX = "usdt";
	private static final String STREAM_SUFFIX = "@kline_";

	public void registerKlineSession(WebSocketSession session) {
		this.session = session;
	}

	public void subKlineForSymbol(String symbol, String interval) throws IOException {
		String param = symbol + SYMBOL_SUFFIX + STREAM_SUFFIX + interval;
		sendMessage("SUBSCRIBE", param);
	}

	public void unSubKlineForSymbol(String symbol, String interval) throws IOException {
		String param = symbol + SYMBOL_SUFFIX + STREAM_SUFFIX + interval;
		sendMessage("UNSUBSCRIBE", param);
	}

	private void sendMessage(String method, String param) throws IOException {
		SubDto subDto = new SubDto(method, List.of(param), session.getId());

		String req = objectMapper.writeValueAsString(subDto);
		try {
			session.sendMessage(new TextMessage(req));
		} catch (Exception e) {
			log.info("Error Message: {}", e.getMessage());
		}
	}
}
