package com.zunza.buythedip.crypto.converter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<String> dataList) {
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(dataList);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("List to JSON 변환 실패", e);
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String data) {
		if (data == null || data.isEmpty()) {
			return new ArrayList<>();
		}
		try {
			return objectMapper.readValue(data, new TypeReference<List<String>>() {});
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON to List 변환 실패", e);
		}
	}
}

