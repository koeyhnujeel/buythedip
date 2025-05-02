package com.zunza.buythedip.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.user.dto.LoginRequestDto;
import com.zunza.buythedip.user.dto.LoginSuccessResponseDto;
import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.DuplicateCheckType;
import com.zunza.buythedip.user.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
		@RequestBody LoginRequestDto loginRequestDto,
		HttpServletResponse response
	) {
		Map<String, String> result = authService.login(loginRequestDto);

		Cookie refreshTokenCookie = new Cookie("refreshToken", result.get("refreshToken"));
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge((int) (7 * 24 * 60 * 60 * 1000));

		response.addCookie(refreshTokenCookie);

		return ResponseEntity.ok(LoginSuccessResponseDto.of(result.get("nickname"), result.get("accessToken")));
	}
}
