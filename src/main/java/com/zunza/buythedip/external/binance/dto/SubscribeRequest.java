package com.zunza.buythedip.external.binance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribeRequest {
	private String method;
	private List<String> params;
	private String id;
}
