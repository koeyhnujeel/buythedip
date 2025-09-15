package com.zunza.buythedip.crypto.entity;

import java.util.List;

import com.zunza.buythedip.common.BaseEntity;
import com.zunza.buythedip.crypto.converter.StringListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CryptoMetadata extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crypto_id", nullable = false)
	private Crypto crypto;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;

	@Lob
	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> website;

	@Lob
	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> twitter;

	@Lob
	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> explorer;

	@Lob
	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringListConverter.class)
	private List<String> tags;
}
