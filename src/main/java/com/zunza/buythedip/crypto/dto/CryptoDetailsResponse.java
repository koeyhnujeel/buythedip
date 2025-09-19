package com.zunza.buythedip.crypto.dto;

import java.util.List;

import com.zunza.buythedip.crypto.entity.CryptoMetadata;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CryptoDetailsResponse {
	private String description;
	private List<String> website;
	private List<String> twitter;
	private List<String> explorer;
	private List<String> tags;

	public static CryptoDetailsResponse createFrom(CryptoMetadata metadata) {
		return CryptoDetailsResponse.builder()
			.description(metadata.getDescription())
			.website(metadata.getWebsite())
			.twitter(metadata.getTwitter())
			.explorer(metadata.getExplorer())
			.tags(metadata.getTags())
			.build();
	}
}
