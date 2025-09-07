package com.zunza.buythedip.watchlist.entity

import com.zunza.buythedip.common.BaseEntity
import com.zunza.buythedip.crypto.entity.Crypto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class WatchlistItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watchlist_id", nullable = false)
    var watchlist: Watchlist? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_id", nullable = false)
    val crypto: Crypto,

    @Column(nullable = false)
    val sortOrder: Int = 0
) : BaseEntity() {
    companion object{
        fun createOf(watchlist: Watchlist, crypto: Crypto, sortOrder: Int): WatchlistItem {
            return WatchlistItem(
                watchlist = watchlist,
                crypto = crypto,
                sortOrder = sortOrder
            )
        }
    }
}
