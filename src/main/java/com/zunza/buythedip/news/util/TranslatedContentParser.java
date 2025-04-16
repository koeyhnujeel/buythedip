package com.zunza.buythedip.news.util;

import java.util.HashMap;
import java.util.Map;

public class TranslatedContentParser {

	public static Map<String, String> parse(String response) {

		Map<String, String> result = new HashMap<>();

		String[] lines = response.split("\n");
		for (String line : lines) {
			if (line.startsWith("headline:")) {
				String headline = line.replace("headline:", "").trim();
				result.put("headline", headline);
			} else if (line.startsWith("summary:")) {
				String summary = line.replace("summary:", "").trim();
				result.put("summary", summary);
			}
		}
		return result;
	}
}
