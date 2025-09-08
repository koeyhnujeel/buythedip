package com.zunza.buythedip.util

import com.zunza.buythedip.crypto.entity.Crypto
import com.zunza.buythedip.user.constant.UserType
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.watchlist.entity.Watchlist
import com.zunza.buythedip.watchlist.entity.WatchlistItem

object TestDataFactory {
    fun createUser(email: String, nickname: String): User =
        User(email = email, password = "password1!", nickname = nickname, type = UserType.NORMAL)

    fun createCrypto(name: String, symbol: String, logo: String): Crypto =
        Crypto(
            name = name,
            symbol = symbol,
            logo = logo,
        )

    fun createWatchlist(user: User?, name: String, sortOrder: Int, isDefault: Boolean = false, isSystem: Boolean = false): Watchlist =
        Watchlist(
            name = name,
            user = user,
            isDefault = isDefault,
            isSystem = isSystem,
            sortOrder = sortOrder
        )

    fun createWatchlistItem(crypto: Crypto, sortOrder: Int): WatchlistItem {
        return WatchlistItem(
            crypto = crypto,
            sortOrder = sortOrder
        )
    }
}
