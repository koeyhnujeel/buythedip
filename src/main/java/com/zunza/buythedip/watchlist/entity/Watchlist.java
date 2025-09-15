package com.zunza.buythedip.watchlist.entity;

import java.util.ArrayList;
import java.util.List;

import com.zunza.buythedip.common.BaseEntity;
import com.zunza.buythedip.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Watchlist extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private boolean isDefault;

	@Column(nullable = false)
	private boolean isSystem;

	@Column(nullable = false)
	private int sortOrder;

	@OneToMany(
		mappedBy = "watchlist",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<WatchlistItem> watchlistItems = new ArrayList<>();

	@Builder
	private Watchlist(String name, User user, boolean isDefault, boolean isSystem, int sortOrder) {
		this.name = name;
		this.user = user;
		this.isDefault = isDefault;
		this.isSystem = isSystem;
		this.sortOrder = sortOrder;
	}

	public static Watchlist createDefaultWatchlist(User user) {
		return Watchlist.builder()
			.name("레드 리스트")
			.user(user)
			.isDefault(true)
			.isSystem(false)
			.sortOrder(0)
			.build();
	}

	public void addWatchlistItem(WatchlistItem... watchlistItems) {
		for (WatchlistItem watchlistItem : watchlistItems) {
			this.watchlistItems.add(watchlistItem);
			watchlistItem.setWatchlist(this);
		}
	}
}
