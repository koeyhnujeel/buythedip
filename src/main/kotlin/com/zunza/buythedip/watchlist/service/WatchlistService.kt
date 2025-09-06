package com.zunza.buythedip.watchlist.service

import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.watchlist.entity.Watchlist
import com.zunza.buythedip.watchlist.entity.WatchlistItem
import com.zunza.buythedip.watchlist.repository.WatchlistRepository
import jakarta.transaction.Transactional

class WatchlistService(
    private val watchlistRepository: WatchlistRepository,
    private val cryptoRepository: CryptoRepository
) {
    /**
     * TODO: findBySymbols 캐시
     */
    @Transactional
    fun createDefaultWatchlist(user: User) {
        val symbols = listOf("BTC", "ETH", "XRP", "BNB", "SOL", "DOGE", "TRX", "ADA")
        val cryptos = cryptoRepository.findBySymbols(symbols)
        val cryptoMap = cryptos.associateBy { it.symbol }

        val watchlist = Watchlist.createDefaultWatchlist(user)
        val watchlistItems = symbols.mapIndexed { index, symbol ->
            val crypto = requireNotNull(cryptoMap[symbol])
            WatchlistItem.createOf(watchlist, crypto, index)
        }.toTypedArray()

        watchlist.addItems(*watchlistItems)
        watchlistRepository.save(watchlist)
    }
}
