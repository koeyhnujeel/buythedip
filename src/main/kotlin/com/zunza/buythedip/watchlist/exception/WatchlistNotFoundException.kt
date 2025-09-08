package com.zunza.buythedip.watchlist.exception

import com.zunza.buythedip.common.CustomException
import org.springframework.http.HttpStatus

class WatchlistNotFoundException(
    private val watchlistId: Long
) :  CustomException(
    MESSAGE.format(watchlistId)
){
    companion object {
        private const val MESSAGE = "왓치리스트를 찾을 수 없습니다 WATCHLIST ID: %d"
    }

    override fun getStatusCode() = HttpStatus.NOT_FOUND.value()
}
