package com.zunza.buythedip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zunza.buythedip.auth.jwt.JwtAuthenticationFilter;
import com.zunza.buythedip.auth.jwt.JwtExceptionFilter;
import com.zunza.buythedip.auth.oauth2.CustomOAuth2FailureHandler;
import com.zunza.buythedip.auth.oauth2.CustomOAuth2SuccessHandler;
import com.zunza.buythedip.auth.oauth2.CustomOAuth2UserService;
import com.zunza.buythedip.auth.oauth2.repository.RedisOAuth2AuthorizationRequestRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final RedisOAuth2AuthorizationRequestRepository redisOAuth2AuthorizationRequestRepository;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionFilter jwtExceptionFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(
				"/error",
				"/favicon.ico",
				"/css/**",
				"/js/**"
				// "/actuator/health"
			);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo ->
					userInfo.userService(customOAuth2UserService)
				)
				.authorizationEndpoint(auth -> auth
					.authorizationRequestRepository(redisOAuth2AuthorizationRequestRepository)
				)
				.successHandler(customOAuth2SuccessHandler)
				.failureHandler(customOAuth2FailureHandler)
			)

			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/api/watchlists/**").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/api/watchlists/**").authenticated()
				.anyRequest().permitAll()
			)

			.addFilterBefore(
				jwtExceptionFilter,
				UsernamePasswordAuthenticationFilter.class
			)

			.addFilterBefore(
				jwtAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class
			)

			// .exceptionHandling(exception -> exception
			// 	.authenticationEntryPoint(jwtAuthenticationEntryPoint())
			// 	.accessDeniedHandler(jwtAccessDeniedHandler())
			// )

			.build();
	}
}
