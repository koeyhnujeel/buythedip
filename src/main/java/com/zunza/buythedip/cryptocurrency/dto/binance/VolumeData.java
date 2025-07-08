package com.zunza.buythedip.cryptocurrency.dto.binance;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VolumeData {
	private long time;
	private double value;
	private String color;

	@Builder
	public VolumeData(long time, double value, String color) {
		this.time = time;
		this.value = value;
		this.color = color;
	}
}
