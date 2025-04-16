package com.zunza.buythedip.constant;

public class RabbitMQConstants {
	public static final String PUBLIC_EXCHANGE = "public.exchange";

	public static final String NEWS_TRANSLATION_QUEUE = "news.translation.queue";
	public static final String NEWS_TRANSLATION_ROUTING_KEY = "new.translation.routing.key";

	public static final String NEWS_STORAGE_QUEUE = "news.storage.queue";
	public static final String NEWS_STORAGE_ROUTING_KEY = "new.storage.routing.key";

	public static String getRoutingKey(String queue) {
		return switch (queue) {
			case NEWS_TRANSLATION_QUEUE -> NEWS_TRANSLATION_ROUTING_KEY;
			case NEWS_STORAGE_QUEUE -> NEWS_STORAGE_ROUTING_KEY;
			default -> null;
		};
	}
}

