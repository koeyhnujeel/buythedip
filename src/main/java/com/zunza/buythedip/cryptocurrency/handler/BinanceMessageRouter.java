package com.zunza.buythedip.cryptocurrency.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;

@Component
public class BinanceMessageRouter {

	private final Map<String, PurposeHandler> handlerMap;

	public BinanceMessageRouter(
		AggregationHandler aggregationHandler,
		TopVolumeTickerBroadcastHandler topVolumeTickerBroadcastHandler,
		SymbolTickerBroadcastHandler symbolTickerBroadcastHandler
	) {
		this.handlerMap = Map.of(
			"aggregation", aggregationHandler,
			"topVolume", topVolumeTickerBroadcastHandler,
			"symbolTicker", symbolTickerBroadcastHandler
		);
	}

	public void route(TradeDto tradeDto) {
		for (String s : handlerMap.keySet()) {
			handlerMap.get(s).handle(tradeDto);
		}
	}
}
