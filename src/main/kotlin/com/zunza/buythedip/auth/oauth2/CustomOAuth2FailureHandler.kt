package com.zunza.buythedip.auth.oauth2

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.nio.charset.Charset

@Component
class CustomOAuth2FailureHandler() : SimpleUrlAuthenticationFailureHandler() {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        val errorMessage = URLEncoder.encode(exception?.message, Charset.defaultCharset())
        val redirectionUrl = "http://localhost:5173/login/error?message=$errorMessage"
        response?.sendRedirect(redirectionUrl)
    }
}
