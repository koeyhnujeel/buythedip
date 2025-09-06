package com.zunza.buythedip.watchlist.repository.querydsl

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.zunza.buythedip.watchlist.dto.WatchlistResponse
import com.zunza.buythedip.watchlist.entity.QWatchlist
import org.springframework.stereotype.Component

@Component
class WatchlistQuerydslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : WatchlistQuerydslRepository {
    override fun findWatchlist(userId: Long?): List<WatchlistResponse> {
        val watchlist = QWatchlist.watchlist
        val condition = when (userId) {
            null -> watchlist.isSystem.isTrue
            else -> watchlist.user.id.eq(userId)
        }

        return jpaQueryFactory
            .select(Projections.constructor(
                WatchlistResponse::class.java,
                watchlist.id,
                watchlist.name,
                watchlist.isDefault,
                watchlist.isSystem,
                watchlist.sortOrder
                )
            )
            .from(watchlist)
            .where(condition)
            .orderBy(watchlist.sortOrder.asc())
            .fetch()
    }
}
