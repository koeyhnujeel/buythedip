package com.zunza.buythedip.watchlist.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zunza.buythedip.crypto.entity.Crypto;
import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.repository.UserRepository;
import com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse;
import com.zunza.buythedip.watchlist.dto.WatchlistResponse;
import com.zunza.buythedip.watchlist.entity.Watchlist;
import com.zunza.buythedip.watchlist.entity.WatchlistItem;
import com.zunza.buythedip.watchlist.repository.WatchlistItemRepository;
import com.zunza.buythedip.watchlist.repository.WatchlistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WatchlistService {
	private final WatchlistItemRepository watchlistItemRepository;
	private final WatchlistRepository watchlistRepository;
	private final CryptoRepository cryptoRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createDefaultWatchlist(User user) {
		List<String> symbols = List.of("BTC", "ETH", "XRP", "BNB", "SOL", "DOGE", "TRX", "ADA");
		List<Crypto> cryptos = cryptoRepository.findBySymbols(symbols);
		Map<String, Crypto> cryptoMap = cryptos.stream()
			.collect(Collectors.toMap(Crypto::getSymbol, crypto -> crypto));

		Watchlist watchlist = Watchlist.createDefaultWatchlist(user);
		WatchlistItem[] watchlistItems = IntStream.range(0, symbols.size())
			.mapToObj(i -> {
				String symbol = symbols.get(i);
				Crypto crypto = cryptoMap.get(symbol);
				return WatchlistItem.createOf(crypto, i);
			})
			.toArray(WatchlistItem[]::new);

		watchlist.addWatchlistItem(watchlistItems);
		watchlistRepository.save(watchlist);
	}

	@Transactional(readOnly = true)
	public List<WatchlistResponse> getWatchlist(Long userId) {
		return watchlistRepository.findWatchlistsByUserId(userId);
	}

	@Transactional(readOnly = true)
	public List<WatchlistDetailsResponse> getWatchlistDetails(Long watchlistId) {
		return watchlistRepository.findWatchlistDetailsById(watchlistId);
	}
}
