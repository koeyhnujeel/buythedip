package com.zunza.buythedip.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.zunza.buythedip.common.ApiResponse
import com.zunza.buythedip.common.ErrorResponse
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter

class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            val errorResponse = ErrorResponse(e.message)
            val apiResponse = ApiResponse(errorResponse, HttpStatus.UNAUTHORIZED.value())

            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = MediaType.APPLICATION_JSON.toString()
            response.characterEncoding = "UTF-8"

            objectMapper.writeValue(response.writer, apiResponse)
        }
    }
}
