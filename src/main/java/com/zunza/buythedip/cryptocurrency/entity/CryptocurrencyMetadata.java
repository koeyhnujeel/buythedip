package com.zunza.buythedip.cryptocurrency.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.zunza.buythedip.cryptocurrency.converter.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CryptocurrencyMetadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String logo;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> website;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> twitter;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> explorer;

	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> tagNames;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToOne
	@JoinColumn(name = "cryptocurrency_id")
	private Cryptocurrency cryptocurrency;

	@Builder
	private CryptocurrencyMetadata(String logo, String description, List<String> website, List<String> twitter,
		List<String> explorer, List<String> tagNames
	) {
		this.logo = logo;
		this.description = description;
		this.website = website == null ? new ArrayList<>() : website;
		this.twitter = twitter == null ? new ArrayList<>() : twitter;
		this.explorer = explorer == null ? new ArrayList<>() : explorer;
		this.tagNames = tagNames == null ? new ArrayList<>() : tagNames;
	}

	public static CryptocurrencyMetadata of(String logo, String description, List<String> website, List<String> twitter,
		List<String> explorer, List<String> tagNames
	) {
		return CryptocurrencyMetadata.builder()
			.logo(logo)
			.description(description)
			.website(website)
			.twitter(twitter)
			.explorer(explorer)
			.tagNames(tagNames)
			.build();
	}
}
