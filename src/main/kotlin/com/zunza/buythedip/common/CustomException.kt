package com.zunza.buythedip.common

abstract class CustomException(
    message: String
) : RuntimeException(message) {
    abstract fun getStatusCode(): Int
}
