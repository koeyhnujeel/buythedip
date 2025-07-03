package com.zunza.buythedip.cryptocurrency.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Cryptocurrency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String symbol;

	private String status;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;


	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "metadata_id")
	private CryptocurrencyMetadata metadata;

	@Builder
	private Cryptocurrency(String name, String symbol, String status, CryptocurrencyMetadata metadata) {
		this.name = name;
		this.symbol = symbol;
		this.status = status;
		this.metadata = metadata;
	}

	public static Cryptocurrency of(String name, String symbol, String status, CryptocurrencyMetadata metadata) {
		return Cryptocurrency.builder()
			.name(name)
			.symbol(symbol)
			.status(status)
			.metadata(metadata)
			.build();
	}
}
