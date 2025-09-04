package com.zunza.buythedip.auth.oauth2

import com.zunza.buythedip.auth.jwt.JwtTokenProvider
import com.zunza.buythedip.user.repository.RefreshJwtRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.nio.charset.Charset

@Component
class CustomOAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshJwtRepository: RefreshJwtRepository
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val oAuth2User = authentication?.principal as CustomOAuth2User

        val userId = oAuth2User.id
        val nickname = oAuth2User.name

        val accessJwt = jwtTokenProvider.generateAccessJwt(userId, oAuth2User.authorities)
        val refreshJwt = jwtTokenProvider.generateRefreshJwt(userId)
        refreshJwtRepository.save(userId, refreshJwt)

        val encodedNickname = URLEncoder.encode(nickname, Charset.defaultCharset())
        val redirectionUrl = "http://localhost:5173/login?token=$accessJwt&nickname=$encodedNickname"

        response?.sendRedirect(redirectionUrl)
    }
}
