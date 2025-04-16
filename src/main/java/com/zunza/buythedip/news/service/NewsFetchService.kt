package com.zunza.buythedip.news.service

import com.zunza.buythedip.constant.RabbitMQConstants
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService
import com.zunza.buythedip.news.dto.NewsDto
import com.zunza.buythedip.news.entity.MinId
import com.zunza.buythedip.news.repository.MinIdRepository
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.models.MarketNews
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NewsFetchService(
    private val rabbitMQService: RabbitMQService,
    private val finnHubClient: DefaultApi,
    private val minIdRepository: MinIdRepository
) {
    @Scheduled(fixedRate = 10000)
    fun fetchNews() {
        Topic.entries.forEach { topic ->
            val minId = getMinId(topic)
            val latestId = minId.latestId
            val newsList = finnHubClient.marketNews(topic.value, latestId)

            if (newsList.isNotEmpty()) {
                val newsDtoList = newsList.stream()
                    .map { NewsDto.from(it) }
                    .toList()

                rabbitMQService.publishMessage(
                    RabbitMQConstants.PUBLIC_EXCHANGE,
                    RabbitMQConstants.NEWS_TRANSLATION_ROUTING_KEY,
                    newsDtoList
                )

                updateMinId(minId, newsList)
            }
        }
    }

    private fun getMinId(topic: Topic): MinId {
        return minIdRepository.findByTopic(topic)
            .orElseGet{ minIdRepository.save(MinId(topic)) }
    }

    private fun updateMinId(minId: MinId, newsList: List<MarketNews>) {
        val newLatestId = newsList.stream()
            .mapToLong { it.id!! }
            .max()

        minId.updateLatestId(newLatestId.asLong)
        minIdRepository.save(minId)
    }
}

enum class Topic(val value: String) {
    GENERAL("general"),
    CRYPTO("crypto")
}

