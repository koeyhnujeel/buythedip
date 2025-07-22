package com.zunza.buythedip.infrastructure.redis.subhandle;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.KlineControlMessage;
import com.zunza.buythedip.cryptocurrency.service.RedisLeaderElection;
import com.zunza.buythedip.cryptocurrency.stream.KlineStreamManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KlineControlHandler implements RedisMessageHandler {

	private final ObjectMapper objectMapper;
	private final KlineStreamManager klineStreamManager;
	private final RedisLeaderElection redisLeaderElection;


	@Override
	public void handle(String message) throws IOException {
		if (!redisLeaderElection.isLeader()) {
			return;
		}

		KlineControlMessage klineControlMessage = objectMapper.readValue(message, KlineControlMessage.class);

		switch (klineControlMessage.getType()) {
			case SUBSCRIBE:
				klineStreamManager.subKlineForSymbol(klineControlMessage.getSymbol(),
					klineControlMessage.getInterval());
				break;
			case UNSUBSCRIBE:
				klineStreamManager.unSubKlineForSymbol(klineControlMessage.getSymbol(),
					klineControlMessage.getInterval());
				break;
		}
	}
}
