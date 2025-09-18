package com.zunza.buythedip.external.binance.dto.deserializer;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zunza.buythedip.external.binance.dto.KlineRestApiResponse;

public class KlineDeserializer extends JsonDeserializer<KlineRestApiResponse> {

	@Override
	public KlineRestApiResponse deserialize(
		JsonParser p,
		DeserializationContext ctxt
	) throws IOException {
		Object[] node = p.readValueAs(Object[].class);

		return new KlineRestApiResponse(
			((Number) node[0]).longValue(),
			new BigDecimal((String) node[1]),
			new BigDecimal((String) node[2]),
			new BigDecimal((String) node[3]),
			new BigDecimal((String) node[4]),
			new BigDecimal((String) node[5]),
			((Number) node[6]).longValue(),
			new BigDecimal((String) node[7]),
			((Number) node[8]).longValue(),
			new BigDecimal((String) node[9]),
			new BigDecimal((String) node[10]),
			new BigDecimal((String) node[11])
		);
	}
}
