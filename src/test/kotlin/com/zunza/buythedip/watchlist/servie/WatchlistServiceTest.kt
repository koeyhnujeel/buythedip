package com.zunza.buythedip.watchlist.servie

import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.user.constant.UserType
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.exception.UserNotFoundException
import com.zunza.buythedip.user.repository.UserRepository
import com.zunza.buythedip.watchlist.dto.CreateWatchlistRequest
import com.zunza.buythedip.watchlist.repository.WatchlistRepository
import com.zunza.buythedip.watchlist.service.WatchlistService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class WatchlistServiceTest() : BehaviorSpec({

    val watchlistRepository = mockk<WatchlistRepository>()
    val cryptoRepository = mockk<CryptoRepository>()
    val userRepository = mockk<UserRepository>()
    val watchlistService = WatchlistService(watchlistRepository, cryptoRepository, userRepository)


    Given("유효한 userId와 createWatchlistRequest가 주어졌을 때") {
        val user = User(
            id = 1L,
            email = "test@email.com",
            password = "password1!",
            nickname = "tester",
            type = UserType.NORMAL
            )

        val request = CreateWatchlistRequest("와치리스트", 1)

        every { userRepository.findByIdOrNull(any()) } returns user
        every { watchlistRepository.save(any()) } returns mockk()
        When("createWatchlist()를 호출하면") {
            watchlistService.createWatchlist(user.id, request)

            Then("watchlistRepository.save가 호출된다") {
                verify(exactly = 1) { userRepository.findByIdOrNull(any()) }
                verify(exactly = 1) { watchlistRepository.save(any()) }
            }
        }
    }

    Given("존재하지 않는 userId가 주어졌을 때") {
        val request = CreateWatchlistRequest("와치리스트", 1)

        every { userRepository.findByIdOrNull(any()) } returns null

        When("createWatchlist()를 호출하면") {
            Then("UserNotFoundException이 발생한다") {
                shouldThrow<UserNotFoundException> {
                    watchlistService.createWatchlist(999L, request)
                }
            }
        }
    }
})
