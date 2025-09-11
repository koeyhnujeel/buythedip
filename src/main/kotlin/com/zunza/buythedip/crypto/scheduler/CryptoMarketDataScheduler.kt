package com.zunza.buythedip.crypto.scheduler

import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.external.binance.client.BinanceClient
import com.zunza.buythedip.infrastructure.redis.constant.RedisKey
import com.zunza.buythedip.infrastructure.redis.service.ReactiveCacheService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

private val logger = KotlinLogging.logger {  }

@Component
class CryptoMarketDataScheduler(
    private val binanceClient: BinanceClient,
    private val cryptoRepository: CryptoRepository,
    private val reactiveCacheService: ReactiveCacheService
) {
    companion object {
        private const val SYMBOL_SUFFIX = "USDT"
    }

    @Scheduled(cron = "3 0 0 * * *", zone = "UTC")
    fun cacheDailyOpenPrice() {
        val startTime = System.currentTimeMillis()
        logger.info{"Open Price 캐싱 작업을 시작합니다."}

        val symbolsMono: Mono<List<String>> = Mono.fromCallable {
            cryptoRepository.findAll().map { it.symbol + SYMBOL_SUFFIX }
        }

        symbolsMono
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { symbols -> Flux.fromIterable(symbols) }
            .flatMap { symbol ->
                binanceClient.getKline(symbol, "1d", 1)
                    .flatMap { klines ->
                        val openPrice = klines.first().open
                        val key = RedisKey.OPEN_PRICE_KEY_PREFIX.value + symbol

                        reactiveCacheService.set(key, openPrice.toString())
                            .doOnSuccess {
                                logger.info { "[성공] [symbol]: $symbol | [open price]: $openPrice" }
                            }
                    }
                    .doOnError { error ->
                        logger.error { "[실패] [symbol]: $symbol | [에러]: ${error.message}" }
                    }
            }
//            .doOnComplete { logger.info{"모든 심볼의 Open Price 캐싱이 완료되었습니다."} } // 작업 지시가 완료
            .doFinally { signalType -> // 성공적으로 끝나든, 에러로 끝나든 항상 마지막에 호출
                val endTime = System.currentTimeMillis()
                logger.info { "Open Price 캐싱 작업이 최종적으로 완료되었습니다. (총 소요 시간: ${endTime - startTime}ms) [종료 타입: $signalType]" }
            }
            .subscribe()
    }
}
