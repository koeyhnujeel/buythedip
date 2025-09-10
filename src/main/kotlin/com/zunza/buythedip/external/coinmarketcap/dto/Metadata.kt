package com.zunza.buythedip.external.coinmarketcap.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Metadata(
    val data: Map<String, List<MetadataDetail>>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MetadataDetail(
    val name: String,
    val symbol: String,
    val description: String?,
    val logo: String,
    val urls: Urls,
    val tags: List<String>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Urls(
    val website: List<String>,
    val twitter: List<String>,
    val explorer: List<String>
)
