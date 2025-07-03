package com.zunza.buythedip.cryptocurrency.handler;

import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;

public interface PurposeHandler {
	void handle(TradeDto tradeDto);
}
