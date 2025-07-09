package com.zunza.buythedip.session.manage;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("tradeSubscriptionManager")
public class TradeSubscriptionManager extends AbstractSubscriptionManager {

	public TradeSubscriptionManager(RedisTemplate<String, Object> redisTemplate) {
		super(redisTemplate);
	}

	@Override
	public void onLastSubscriber(String destination, Long subscriberCount) {}
}
