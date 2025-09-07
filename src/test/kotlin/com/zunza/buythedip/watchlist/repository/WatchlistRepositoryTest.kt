package com.zunza.buythedip.watchlist.repository

import com.zunza.buythedip.config.JacksonTestConfig
import com.zunza.buythedip.config.QuerydslConfig
import com.zunza.buythedip.config.TestContainersConfig
import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.repository.UserRepository
import com.zunza.buythedip.util.TestDataFactory
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
    @Autowired private val cryptoRepository: CryptoRepository,
    @Autowired private var watchlistRepository: WatchlistRepository,
) : BehaviorSpec() {

    lateinit var user1: User
    lateinit var user2: User
    lateinit var watchlist1: Watchlist
    lateinit var watchlist2: Watchlist

    init {
        beforeSpec {
            val btc = TestDataFactory.createCrypto("Bitcoin", "BTC", "B")
            val eth = TestDataFactory.createCrypto("Ethereum", "ETH", "E")

            val xrp = TestDataFactory.createCrypto("Ripple", "XRP", "X")
            val sol = TestDataFactory.createCrypto("Solana", "SOL", "S")
            val bnb = TestDataFactory.createCrypto("BNB", "BNB", "B")
            val ada = TestDataFactory.createCrypto("Cardano", "ADA", "A")
            cryptoRepository.saveAll(listOf(btc, eth, xrp, sol, bnb, ada))

            val wb = TestDataFactory.createWatchlistItem(btc, 0)
            val we = TestDataFactory.createWatchlistItem(eth, 1)

            val wx = TestDataFactory.createWatchlistItem(xrp, 0)
            val ws = TestDataFactory.createWatchlistItem(sol, 1)
            val wbb = TestDataFactory.createWatchlistItem(bnb, 3)
            val wa = TestDataFactory.createWatchlistItem(ada, 2)

            user1 = userRepository.save(TestDataFactory.createUser("user1@email.com", "user1"))
            user2 = userRepository.save(TestDataFactory.createUser("user2@email.com", "user2"))

            val systemWatchlist = TestDataFactory.createWatchlist(null, "시스템 레드 리스트", 0, true, true)
            val user1RedList = TestDataFactory.createWatchlist(name = "유저1 레드 리스트", user = user1, isDefault = true, isSystem = false, sortOrder = 0)
            val user2RedList = TestDataFactory.createWatchlist(name = "유저2 레드 리스트", user = user2, isDefault = false, isSystem = false, sortOrder = 0)
            watchlistRepository.saveAll(listOf(systemWatchlist, user1RedList, user2RedList))

            val user1Watchlist = TestDataFactory.createWatchlist(name = "유저1 왓치리스트", user = user1, isDefault = false, isSystem = false, sortOrder = 1)
            user1Watchlist.addItems(wb, we)
            watchlist1 = watchlistRepository.save(user1Watchlist)

            val user2watchlist = TestDataFactory.createWatchlist(name = "유저2 왓치리스트", user = user2, isDefault = true, isSystem = false, sortOrder = 1)
            user2watchlist.addItems(wx, ws, wbb, wa)
            watchlist2 = watchlistRepository.save(user2watchlist)
        }

        Given("사용자 ID가 null로 주어졌을 때 (비로그인 상태)") {
            When("findWatchlist 메소드를 호출하면") {
                val result = watchlistRepository.findWatchlist(null)

                Then("isSystem이 true인 왓치리스트가 반환되어야 한다") {
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

        Given("특정 왓치리스트 ID가 주어졌을 때") {
            When("findWatchlistDetailsById 메소드를 호출하면") {
                val result1 = watchlistRepository.findWatchlistDetailsById(watchlist1.id)
                val result2 = watchlistRepository.findWatchlistDetailsById(watchlist2.id)

                Then("해당 왓치리스트의 종목들이 WatchlistItem.sortOrder 오름차순으로 반환 되어야 한다.") {
                    result1 shouldHaveSize 2
                    result1[0].name shouldBe "Bitcoin"
                    result1[0].symbol shouldBe "BTC"
                    result1[0].logo shouldBe "B"
                    result1[1].name shouldBe "Ethereum"
                    result1[1].symbol shouldBe "ETH"
                    result1[1].logo shouldBe "E"

                    result2 shouldHaveSize 4
                    result2[0].name shouldBe "Ripple"
                    result2[0].symbol shouldBe "XRP"
                    result2[0].logo shouldBe "X"
                    result2[0].sortOrder shouldBe 0
                    result2[1].name shouldBe "Solana"
                    result2[1].symbol shouldBe "SOL"
                    result2[1].logo shouldBe "S"
                    result2[1].sortOrder shouldBe 1
                    result2[2].name shouldBe "Cardano"
                    result2[2].symbol shouldBe "ADA"
                    result2[2].logo shouldBe "A"
                    result2[2].sortOrder shouldBe 2
                    result2[3].name shouldBe "BNB"
                    result2[3].symbol shouldBe "BNB"
                    result2[3].logo shouldBe "B"
                    result2[3].sortOrder shouldBe 3
                }
            }
        }
    }
}
