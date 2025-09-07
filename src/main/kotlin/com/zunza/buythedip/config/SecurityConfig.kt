package com.zunza.buythedip.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.zunza.buythedip.auth.jwt.JwtAuthenticationFilter
import com.zunza.buythedip.auth.jwt.JwtExceptionFilter
import com.zunza.buythedip.auth.jwt.JwtTokenProvider
import com.zunza.buythedip.auth.oauth2.CustomOAuth2FailureHandler
import com.zunza.buythedip.auth.oauth2.CustomOAuth2SuccessHandler
import com.zunza.buythedip.auth.oauth2.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customOAuth2SuccessHandler: CustomOAuth2SuccessHandler,
    private val customOAuth2FailureHandler: CustomOAuth2FailureHandler
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer =
        WebSecurityCustomizer { web: WebSecurity? ->
            web!!.ignoring()
                .requestMatchers("/error", "/favicon.ico")

    }

    /**
     * TODO: AccessDenied Handler 추가하기
     */
    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            .oauth2Login { oAuth2 -> oAuth2
                .userInfoEndpoint { config ->
                    config.userService(customOAuth2UserService)
                }
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
            }

            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "/api/watchlists/**").authenticated()
                    .anyRequest().permitAll()
            }

            .addFilterBefore(
                JwtExceptionFilter(objectMapper),
                UsernamePasswordAuthenticationFilter::class.java
            )

            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}
