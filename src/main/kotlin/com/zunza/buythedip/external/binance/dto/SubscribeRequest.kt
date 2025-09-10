package com.zunza.buythedip.external.binance.dto

data class SubscribeRequest(
    val method: String,
    val params: List<String>,
    val id: String
)
