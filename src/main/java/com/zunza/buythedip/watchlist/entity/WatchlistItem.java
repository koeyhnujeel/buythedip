package com.zunza.buythedip.watchlist.entity;

import com.zunza.buythedip.common.BaseEntity;
import com.zunza.buythedip.crypto.entity.Crypto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WatchlistItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "watchlist_id", nullable = false)
	private Watchlist watchlist;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crypto_id", nullable = false)
	private Crypto crypto;

	@Column(nullable = false)
	private int sortOrder;

	@Builder
	private WatchlistItem(Watchlist watchlist, Crypto crypto, int sortOrder) {
		this.watchlist = watchlist;
		this.crypto = crypto;
		this.sortOrder = sortOrder;
	}

	public static WatchlistItem createOf(
		Crypto crypto,
		int sortOrder
	) {
		return WatchlistItem.builder()
			.crypto(crypto)
			.sortOrder(sortOrder)
			.build();
	}

	public void setWatchlist(Watchlist watchlist) {
		this.watchlist = watchlist;
	}
}
