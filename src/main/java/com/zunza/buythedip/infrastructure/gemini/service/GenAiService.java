package com.zunza.buythedip.infrastructure.gemini.service;

import org.springframework.stereotype.Service;

import com.google.genai.Client;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenAiService {

	private final Client genaiClient;

	private static final String MODEL = "gemini-2.0-flash-lite";

	public String generate(String prompt) {
		return genaiClient.models.generateContent(MODEL, prompt, null).text();
	}
}
