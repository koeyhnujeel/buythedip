package com.zunza.buythedip.watchlist.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zunza.buythedip.crypto.entity.Crypto;
import com.zunza.buythedip.crypto.exception.CryptoNotFoundException;
import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;
import com.zunza.buythedip.watchlist.dto.WatchlistCreateRequest;
import com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse;
import com.zunza.buythedip.watchlist.dto.WatchlistItemAddRequest;
import com.zunza.buythedip.watchlist.dto.WatchlistResponse;
import com.zunza.buythedip.watchlist.entity.Watchlist;
import com.zunza.buythedip.watchlist.entity.WatchlistItem;
import com.zunza.buythedip.watchlist.exception.WatchlistAccessDeniedException;
import com.zunza.buythedip.watchlist.exception.WatchlistItemNotFoundException;
import com.zunza.buythedip.watchlist.exception.WatchlistNotFoundException;
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

	@Transactional
	public void createWatchlist(
		Long userId,
		WatchlistCreateRequest watchlistCreateRequest
	) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		watchlistRepository.save(Watchlist.createOf(user, watchlistCreateRequest));
	}

	@Transactional
	public void deleteWatchlist(
		Long userId,
		Long watchlistId
	) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		Watchlist watchlist = watchlistRepository.findById(watchlistId)
			.orElseThrow(WatchlistNotFoundException::new);

		if (!watchlist.getUser().getId().equals(user.getId())) {
			throw new WatchlistAccessDeniedException();
		}

		watchlistRepository.delete(watchlist);
	}

	@Transactional
	public void addWatchlistItem(
		Long watchlistId,
		WatchlistItemAddRequest request
	) {
		Long cryptoId = request.getCryptoId();
		int sortOrder = request.getSortOrder();

		Crypto crypto = cryptoRepository.findById(cryptoId)
			.orElseThrow(CryptoNotFoundException::new);

		WatchlistItem watchlistItem = WatchlistItem.createOf(crypto, sortOrder);

		Watchlist watchlist = watchlistRepository.findById(watchlistId)
			.orElseThrow(WatchlistNotFoundException::new);

		watchlist.addWatchlistItem(watchlistItem);
		watchlistRepository.save(watchlist);
	}

	@Transactional
	public void deleteWatchlistItem(
		Long userId,
		Long watchlistId,
		Long watchlistItemId
	) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		Watchlist watchlist = watchlistRepository.findById(watchlistId)
			.orElseThrow(WatchlistNotFoundException::new);

		if (!watchlist.getUser().getId().equals(user.getId())) {
			throw new WatchlistAccessDeniedException();
		}

		WatchlistItem WatchlistItem = watchlistItemRepository.findById(watchlistItemId)
			.orElseThrow(WatchlistItemNotFoundException::new);

		watchlistItemRepository.delete(WatchlistItem);
	}
}
