package com.zunza.buythedip.user.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.user.dto.LoginRequestDto;
import com.zunza.buythedip.user.dto.LoginSuccessResponseDto;
import com.zunza.buythedip.user.dto.ReissueTokenResponseDto;
import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.DuplicateCheckType;
import com.zunza.buythedip.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/api/auth/signup")
	public ResponseEntity<Void> signup(
		@Valid @RequestBody SignupRequestDto signupRequestDto
	) {
		authService.signup(signupRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/api/auth/check/account-id")
	public ResponseEntity<Void> checkAccountId(
		@RequestParam String accountId
	) {
		authService.validateDuplicateField(DuplicateCheckType.ACCOUNT_ID, accountId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/api/auth/check/nickname")
	public ResponseEntity<Void> checkNickname(
		@RequestParam String nickname
	) {
		authService.validateDuplicateField(DuplicateCheckType.NICKNAME, nickname);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/api/auth/login")
	public ResponseEntity<LoginSuccessResponseDto> login(
		@RequestBody LoginRequestDto loginRequestDto
	) {
		Map<String, String> result = authService.login(loginRequestDto);

		ResponseCookie refreshTokenCookie = setRefreshTokenCookie(result.get("refreshToken"));

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
			.body(LoginSuccessResponseDto.of(result.get("nickname"), result.get("accessToken")));
	}

	@PostMapping("/api/auth/reissue")
	public ResponseEntity<ReissueTokenResponseDto> reissueToken(
		@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
		@CookieValue(name = "refreshToken", required = false) String refreshToken
	) {
		Map<String, String> reissueToken = authService.reissue(authorizationHeader, refreshToken);

		ResponseCookie refreshTokenCookie = setRefreshTokenCookie(reissueToken.get("newRefreshToken"));

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
			.body(ReissueTokenResponseDto.from(reissueToken.get("newAccessToken")));
	}

	private ResponseCookie setRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.path("/")
			.maxAge((int)(7 * 24 * 60 * 60 * 1000))
			.build();
	}

	@PostMapping("/api/auth/logout")
	public ResponseEntity<Void> logout(
		@AuthenticationPrincipal Long userId
	) {
		authService.logout(userId);

		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", null)
			.httpOnly(true)
			.path("/")
			.maxAge(0)
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
			.build();
	}
}
