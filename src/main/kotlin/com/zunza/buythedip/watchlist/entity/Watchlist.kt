package com.zunza.buythedip.watchlist.entity

import com.zunza.buythedip.common.BaseEntity
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.watchlist.dto.WatchlistCreateRequest
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Watchlist(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 20)
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User?,

    @Column(nullable = false)
    val isDefault: Boolean = false,

    @Column(nullable = false)
    val isSystem: Boolean = false,

    @Column(nullable = false)
    val sortOrder: Int = 0,

    @OneToMany(
        mappedBy = "watchlist",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    val watchlistItems: MutableList<WatchlistItem> = mutableListOf()
) : BaseEntity() {
    companion object {
        fun createDefaultWatchlist(user: User): Watchlist {
            return Watchlist(
                name = "레드 리스트",
                user = user,
                isDefault = true,
            )
        }

        fun createOf(user: User, watchlistCreateRequest: WatchlistCreateRequest): Watchlist {
           return Watchlist(
               user = user,
               name = watchlistCreateRequest.name,
               sortOrder = watchlistCreateRequest.sortOrder
           )
        }
    }

    fun addItems(vararg watchlistItems: WatchlistItem) {
        watchlistItems.forEach {
            it.watchlist = this
            this.watchlistItems.add(it)
        }
    }
}
