package com.zunza.buythedip.user.service

import com.zunza.buythedip.auth.jwt.JwtTokenProvider
import com.zunza.buythedip.auth.user.CustomUserDetails
import com.zunza.buythedip.user.dto.EmailAvailableResponse
import com.zunza.buythedip.user.dto.LoginRequest
import com.zunza.buythedip.user.dto.NicknameAvailableResponse
import com.zunza.buythedip.user.dto.SignupRequest
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.exception.LoginFailedException
import com.zunza.buythedip.user.repository.RefreshJwtRepository
import com.zunza.buythedip.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {  }

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val refreshJwtRepository: RefreshJwtRepository
) {
    fun isEmailAvailable(email: String): EmailAvailableResponse {
        validateEmailFormat(email)
        return when (userRepository.existsByEmail(email)) {
            true -> EmailAvailableResponse(false)
            false -> EmailAvailableResponse(true)
        }
    }

    fun isNicknameAvailable(nickname: String): NicknameAvailableResponse {
        validateNicknameFormat(nickname)
        return when (userRepository.existsByNickname(nickname)) {
            true -> NicknameAvailableResponse(false)
            false -> NicknameAvailableResponse(true)
        }
    }

    fun signup(signupRequest: SignupRequest) {
        val encodedPassword = passwordEncoder.encode(signupRequest.password)
        userRepository.save(User.of(
            signupRequest.email,
            signupRequest.nickname,
            encodedPassword
        ))
    }

    fun login(loginRequest: LoginRequest): Map<String, String> {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            loginRequest.email,
            loginRequest.password
        )

        val authentication = authenticateUser(authenticationToken)
        val userDetails = authentication.principal as CustomUserDetails

        val accessJwt = jwtTokenProvider.generateAccessJwt(
            userDetails.userId,
            userDetails.authorities)
        val refreshJwt = jwtTokenProvider.generateRefreshJwt(userDetails.userId)
        refreshJwtRepository.save(userDetails.userId, refreshJwt)

        return mapOf(
            "nickname" to userDetails.nickname,
            "accessJwt" to accessJwt,
            "refreshJwt" to refreshJwt
        )
    }

    fun logout(userId: Long) {
        refreshJwtRepository.delete(userId)
    }

    private fun validateEmailFormat(email: String) {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        if (!emailRegex.matches(email)) {
            logger.warn { "잘못된 이메일 형식입니다: $email" }
            throw IllegalArgumentException("잘못된 이메일 형식입니다.")
        }
    }

    private fun validateNicknameFormat(nickname: String) {
        val nicknameRegex = Regex("^(?=.*[가-힣a-zA-Z])[가-힣a-zA-Z0-9]{2,12}$")
        if (!nicknameRegex.matches(nickname)) {
            logger.warn { "잘못된 닉네임 형식입니다: $nickname" }
            throw IllegalArgumentException("잘못된 닉네임 형식입니다.")
        }
    }

    private fun authenticateUser(
        authenticationToken: UsernamePasswordAuthenticationToken
    ): Authentication {
        try {
            return authenticationManager.authenticate(authenticationToken)
        } catch (e: AuthenticationException) {
            throw LoginFailedException()
        }
    }
}
