package com.zunza.buythedip.watchlist.repository

import com.zunza.buythedip.config.JacksonTestConfig
import com.zunza.buythedip.config.QuerydslConfig
import com.zunza.buythedip.config.TestContainersConfig
import com.zunza.buythedip.user.constant.UserType
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.repository.UserRepository
import com.zunza.buythedip.watchlist.entity.Watchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(QuerydslConfig::class, TestContainersConfig::class, JacksonTestConfig::class)
class WatchlistRepositoryTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private var watchlistRepository: WatchlistRepository
) : BehaviorSpec() {

    lateinit var user1: User
    lateinit var user2: User

    init {
        beforeSpec {
            user1 = userRepository.save(
                User(email = "user1@email.com", password = "password1!", nickname = "user1", type = UserType.NORMAL)
            )
            user2 = userRepository.save(
                User(email = "user2@email.com", password = "password1!", nickname = "user2", type = UserType.NORMAL)
            )

            val systemWatchlist =
                Watchlist(name = "시스템 레드 리스트", user = null, isDefault = true, isSystem = true, sortOrder = 0)

            val user1Watchlist1 =
                Watchlist(name = "유저1 레드 리스트", user = user1, isDefault = true, isSystem = false, sortOrder = 0)
            val user1Watchlist2 =
                Watchlist(name = "유저1 왓치리스트", user = user1, isDefault = false, isSystem = false, sortOrder = 1)

            val user2Watchlist1 =
                Watchlist(name = "유저2 레드 리스트", user = user2, isDefault = false, isSystem = false, sortOrder = 0)
            val user2Watchlist2 =
                Watchlist(name = "유저2 왓치리스트", user = user2, isDefault = true, isSystem = false, sortOrder = 1)

            val watchlists = listOf(systemWatchlist, user1Watchlist1, user1Watchlist2, user2Watchlist1, user2Watchlist2)
            watchlistRepository.saveAll(watchlists)
        }

        Given("사용자 ID가 null로 주어졌을 때 (비로그인 상태)") {
            When("findWatchlist 메소드를 호출하면") {
                val result = watchlistRepository.findWatchlist(null)

                Then("isSystem이 true인 관심목록만 sortOrder 오름차순으로 반환되어야 한다") {
                    result shouldHaveSize 1
                    result[0].name shouldBe "시스템 레드 리스트"
                    result[0].isSystem shouldBe true
                }
            }
        }

        Given("특정 사용자 ID(user1)가 주어졌을 때 (로그인 상태)") {
            When("findWatchlist 메소드를 호출하면") {
                val result = watchlistRepository.findWatchlist(user1.id)

                Then("해당 사용자의 watchlist만 sortOrder 오름차순으로 반환되어야 한다") {
                    result shouldHaveSize 2
                    result[0].name shouldBe "유저1 레드 리스트"
                    result[0].isDefault shouldBe true
                    result[0].sortOrder shouldBe 0
                    result[1].name shouldBe "유저1 왓치리스트"
                    result[1].isDefault shouldBe false
                    result[1].sortOrder shouldBe 1

                    result.any { it.name.contains("시스템 레드 리스트") } shouldBe false
                    result.any { it.name.contains("유저2 왓치리스트") } shouldBe false
                    result.any { it.name.contains("유저2 레드 리스트") } shouldBe false
                }
            }
        }
    }
}
