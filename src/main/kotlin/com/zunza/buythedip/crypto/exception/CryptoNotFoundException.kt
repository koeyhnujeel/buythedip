package com.zunza.buythedip.crypto.exception

import com.zunza.buythedip.common.CustomException
import org.springframework.http.HttpStatus

class CryptoNotFoundException(
    private val cryptoId: Long
) :  CustomException(
    MESSAGE.format(cryptoId)
){
    companion object {
        private const val MESSAGE = "암호화폐를 찾을 수 없습니다 CRYPTO ID: %d"
    }

    override fun getStatusCode() = HttpStatus.NOT_FOUND.value()
}
