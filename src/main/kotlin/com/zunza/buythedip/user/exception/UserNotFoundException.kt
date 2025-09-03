package com.zunza.buythedip.user.exception

import com.zunza.buythedip.common.CustomException
import org.springframework.http.HttpStatus

class UserNotFoundException(
    id: Long
) : CustomException(MESSAGE + id) {

    companion object {
        private const val MESSAGE = "사용자를 찾을 수 없습니다: "
    }

    override fun getStatusCode() =
        HttpStatus.NOT_FOUND.value()
}
