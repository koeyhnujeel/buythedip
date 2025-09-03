package com.zunza.buythedip.user.controller

import com.zunza.buythedip.common.ApiResponse
import com.zunza.buythedip.user.dto.EmailAvailableResponse
import com.zunza.buythedip.user.dto.LoginRequest
import com.zunza.buythedip.user.dto.LoginResponse
import com.zunza.buythedip.user.dto.NicknameAvailableResponse
import com.zunza.buythedip.user.dto.SignupRequest
import com.zunza.buythedip.user.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class AuthController(
    private val authService: AuthService
) {
    @GetMapping("/api/auth/signup/email/validation")
    fun checkEmailDuplication(
        @RequestParam email: String
    ): ResponseEntity<EmailAvailableResponse> =
        ResponseEntity.ok(authService.isEmailAvailable(email))

    @GetMapping("/api/auth/signup/nickname/validation")
    fun checkNicknameDuplication(
        @RequestParam nickname: String
    ): ResponseEntity<NicknameAvailableResponse> =
        ResponseEntity.ok(authService.isNicknameAvailable(nickname))

    @PostMapping("/api/auth/signup")
    fun signup(
        @Valid @RequestBody signupRequest: SignupRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.signup(signupRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build()
    }

    @PostMapping("/api/auth/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<LoginResponse> {
        val loginResultMap: Map<String, String> = authService.login(loginRequest)
        val nickname = loginResultMap["nickname"]!!
        val accessJwt = loginResultMap["accessJwt"]!!
        val refreshJwt = loginResultMap["refreshJwt"]!!
        val responseCookie = buildResponseCookie(refreshJwt, Duration.ofDays(7))

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(LoginResponse(nickname, accessJwt))
    }

    @PostMapping("/api/auth/logout")
    fun logout(
        @AuthenticationPrincipal userId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.logout(userId)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, buildResponseCookie().toString())
            .build()
    }

    private fun buildResponseCookie(
        value: String = "",
        maxAge: Duration = Duration.ZERO
    ): ResponseCookie {
        return ResponseCookie.from("refreshToken", value)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAge)
            .build()
    }
}
