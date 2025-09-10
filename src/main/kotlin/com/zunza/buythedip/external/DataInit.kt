package com.zunza.buythedip.external

import com.zunza.buythedip.crypto.entity.Crypto
import com.zunza.buythedip.crypto.entity.CryptoMetadata
import com.zunza.buythedip.crypto.repository.CryptoMetadataRepository
import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.external.binance.client.BinanceClient
import com.zunza.buythedip.external.binance.dto.Symbol
import com.zunza.buythedip.external.coinmarketcap.client.CoinMarketCapClient
import com.zunza.buythedip.external.coinmarketcap.dto.Metadata
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {  }

//@Component
class DataInit(
    private val binanceClient: BinanceClient,
    private val coinMarketCapClient: CoinMarketCapClient,
    private val cryptoRepository: CryptoRepository,
    private val cryptoMetadataRepository: CryptoMetadataRepository
) {
//    @PostConstruct
    fun dataInit() {
        logger.info { "==============START===============" }

        val exchangeInfo = binanceClient.getExchangeInfo()
        val filtered = filter(exchangeInfo.symbols)

        val baseAssets = filtered.map { it.baseAsset }
        val partitionList = partitionList(baseAssets, 100)

        val result = mutableListOf<Metadata>()
        for (list in partitionList) {
            val symbols = list.joinToString(",")
            logger.info { symbols }
            val metadata = coinMarketCapClient.getInfo(symbols)
            result.add(metadata)
        }

        for (metadata in result) {
            for (s in metadata.data.keys) {

                val details = metadata.data[s]
                if(details.isNullOrEmpty()) continue
                val detail = details[0]
                val urls = detail.urls

                val crypto = Crypto(
                    name = detail.name,
                    symbol = detail.symbol,
                    logo = detail.logo,
                )
                val savedCrypto = cryptoRepository.save(crypto)
                logger.info { savedCrypto }

                val cryptoMetadata = CryptoMetadata(
                    crypto = savedCrypto,
                    description = detail.description ?: "",
                    website = urls.website,
                    twitter = urls.twitter,
                    explorer = urls.explorer,
                    tags = detail.tags ?: emptyList()
                    )
                val savedMeta = cryptoMetadataRepository.save(cryptoMetadata)
                logger.info { savedMeta }
            }
        }
        logger.info { "==============END===============" }
    }

    private fun filter(symbols: List<Symbol>): List<Symbol> {
        return symbols
            .filter { it.quoteAsset == "USDT" }
            .filter { it.status == "TRADING" }
    }

    private fun partitionList(list: List<String>, size: Int): MutableList<List<String>> {
        val result = mutableListOf<List<String>>()
        for (i in 0 until list.size step size) {
            result.add(list.subList(i, (i + size).coerceAtMost(list.size)))
        }
        return result
    }
}
