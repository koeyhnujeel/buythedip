package com.zunza.buythedip.watchlist.repository

import com.zunza.buythedip.config.JacksonTestConfig
import com.zunza.buythedip.config.QuerydslConfig
import com.zunza.buythedip.config.TestContainersConfig
import com.zunza.buythedip.crypto.entity.Crypto
import com.zunza.buythedip.crypto.entity.CryptoMetadata
import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.user.constant.UserType
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.repository.UserRepository
import com.zunza.buythedip.watchlist.entity.Watchlist
import com.zunza.buythedip.watchlist.entity.WatchlistItem
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
            val btcMeta = CryptoMetadata(
                description =  "btc",
                website =  listOf("btc.com"),
                twitter = listOf("btc twitter"),
                explorer = listOf("btc explorer"),
                tagNames = listOf("btc"))

            val btc = Crypto(
                name = "Bitcoin",
                symbol = "BTC",
                logo = "B",
                metadata = btcMeta
            )
            val savedBtc = cryptoRepository.save(btc)

            val ethMeta = CryptoMetadata(
                description =  "eth",
                website =  listOf("eth.com"),
                twitter = listOf("eth twitter"),
                explorer = listOf("eth explorer"),
                tagNames = listOf("eth"))

            val eth = Crypto(
                name = "Ethereum",
                symbol = "ETH",
                logo = "E",
                metadata = ethMeta
            )
            val savedEth = cryptoRepository.save(eth)

            val xrpMeta = CryptoMetadata(
                description =  "xrp",
                website =  listOf("xrp.com"),
                twitter = listOf("xrp twitter"),
                explorer = listOf("xrp explorer"),
                tagNames = listOf("xrp"))

            val xrp = Crypto(
                name = "Ripple",
                symbol = "XRP",
                logo = "X",
                metadata = xrpMeta
            )
            val savedXrp = cryptoRepository.save(xrp)

            val solMeta = CryptoMetadata(
                description =  "sol",
                website =  listOf("sol.com"),
                twitter = listOf("sol twitter"),
                explorer = listOf("sol explorer"),
                tagNames = listOf("sol"))

            val sol = Crypto(
                name = "Solana",
                symbol = "SOL",
                logo = "S",
                metadata = solMeta
            )
            val savedSol = cryptoRepository.save(sol)

            val bnbMeta = CryptoMetadata(
                description =  "bnb",
                website =  listOf("bnb.com"),
                twitter = listOf("bnb twitter"),
                explorer = listOf("bnb explorer"),
                tagNames = listOf("bnb"))

            val bnb = Crypto(
                name = "BNB",
                symbol = "BNB",
                logo = "B",
                metadata = bnbMeta
            )
            val savedBnb = cryptoRepository.save(bnb)

            val adaMeta = CryptoMetadata(
                description =  "ada",
                website =  listOf("ada.com"),
                twitter = listOf("ada twitter"),
                explorer = listOf("ada explorer"),
                tagNames = listOf("ada"))

            val ada = Crypto(
                name = "Cardano",
                symbol = "ADA",
                logo = "A",
                metadata = adaMeta
            )
            val savedAda = cryptoRepository.save(ada)

            val wb = WatchlistItem(crypto = savedBtc, sortOrder = 0)
            val we = WatchlistItem(crypto = savedEth, sortOrder = 1)
            val wx = WatchlistItem(crypto = savedXrp, sortOrder = 0)
            val ws = WatchlistItem(crypto = savedSol, sortOrder = 1)
            val wbb = WatchlistItem(crypto = savedBnb, sortOrder = 2)
            val wa = WatchlistItem(crypto = savedAda, sortOrder = 3)

            user1 = userRepository.save(
                User(email = "user1@email.com", password = "password1!", nickname = "user1", type = UserType.NORMAL)
            )
            user2 = userRepository.save(
                User(email = "user2@email.com", password = "password1!", nickname = "user2", type = UserType.NORMAL)
            )

            val systemWatchlist = Watchlist(name = "시스템 레드 리스트", user = null, isDefault = true, isSystem = true, sortOrder = 0)

            val user1Watchlist1 = Watchlist(name = "유저1 레드 리스트", user = user1, isDefault = true, isSystem = false, sortOrder = 0)
            val user2Watchlist1 = Watchlist(name = "유저2 레드 리스트", user = user2, isDefault = false, isSystem = false, sortOrder = 0)

            val user1Watchlist2 = Watchlist(name = "유저1 왓치리스트", user = user1, isDefault = false, isSystem = false, sortOrder = 1)
            user1Watchlist2.addItems(wb, we)
            watchlist1 = watchlistRepository.save(user1Watchlist2)

            val user2watchlist2 = Watchlist(name = "유저2 왓치리스트", user = user2, isDefault = true, isSystem = false, sortOrder = 1)
            user2watchlist2.addItems(wx, ws, wbb, wa)
            watchlist2 = watchlistRepository.save(user2watchlist2)

            val watchlists = listOf(systemWatchlist, user1Watchlist1, user2Watchlist1)
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
                    result2[2].name shouldBe "BNB"
                    result2[2].symbol shouldBe "BNB"
                    result2[2].logo shouldBe "B"
                    result2[2].sortOrder shouldBe 2
                    result2[3].name shouldBe "Cardano"
                    result2[3].symbol shouldBe "ADA"
                    result2[3].logo shouldBe "A"
                    result2[3].sortOrder shouldBe 3
                }
            }
        }
    }
}
