package com.zunza.buythedip.cryptocurrency.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KlineControlMessage {
	private ActionType type;
	private String symbol;
	private String interval;

	public enum ActionType {
		SUBSCRIBE,
		UNSUBSCRIBE
	}
}
