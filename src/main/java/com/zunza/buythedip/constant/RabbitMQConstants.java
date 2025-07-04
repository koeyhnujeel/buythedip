package com.zunza.buythedip.constant;

public class RabbitMQConstants {
	public static final String PUBLIC_EXCHANGE = "public.exchange";

	public static final String NEWS_TRANSLATION_QUEUE = "news.translation.queue";
	public static final String NEWS_TRANSLATION_ROUTING_KEY = "new.translation.routing.key";

	public static final String NEWS_STORAGE_QUEUE = "news.storage.queue";
	public static final String NEWS_STORAGE_ROUTING_KEY = "new.storage.routing.key";

	public static final String TRADE_AGGREGATION_QUEUE = "trade.aggregation.queue";
	public static final String TRADE_AGGREGATION_ROUTING_KEY = "trade.aggregation.routing.key";

	public static final String TOP_VOLUME_TICKER_BROADCAST_QUEUE = "top.volume.ticker.broadcast.queue";
	public static final String TOP_VOLUME_TICKER_BROADCAST_ROUTING_KEY = "top.volume.ticker.broadcast.routing.key";

	public static final String SYMBOL_TICKER_BROADCAST_QUEUE = "symbol.ticker.broadcast.queue";
	public static final String SYMBOL_TICKER_BROADCAST_ROUTING_KEY = "symbol.ticker.broadcast.routing.key";


	public static String getRoutingKey(String queue) {
		return switch (queue) {
			case NEWS_TRANSLATION_QUEUE -> NEWS_TRANSLATION_ROUTING_KEY;
			case NEWS_STORAGE_QUEUE -> NEWS_STORAGE_ROUTING_KEY;
			case TRADE_AGGREGATION_QUEUE -> TRADE_AGGREGATION_ROUTING_KEY;
			case TOP_VOLUME_TICKER_BROADCAST_QUEUE -> TOP_VOLUME_TICKER_BROADCAST_ROUTING_KEY;
			case SYMBOL_TICKER_BROADCAST_QUEUE -> SYMBOL_TICKER_BROADCAST_ROUTING_KEY;
			default -> null;
		};
	}
}

