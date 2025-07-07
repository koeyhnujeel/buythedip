package com.zunza.buythedip.infrastructure.redis.subhandle;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RedisMessageHandler {
	void handle(String message) throws IOException;
}
