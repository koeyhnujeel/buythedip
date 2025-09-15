package com.zunza.buythedip.user.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.common.ApiResponse;
import com.zunza.buythedip.user.dto.EmailAvailableResponse;
import com.zunza.buythedip.user.dto.LoginRequest;
import com.zunza.buythedip.user.dto.LoginResponse;
import com.zunza.buythedip.user.dto.NicknameAvailableResponse;
import com.zunza.buythedip.user.dto.SignupRequest;
import com.zunza.buythedip.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@GetMapping("/signup/email/validation")
	public ResponseEntity<EmailAvailableResponse> checkEmailDuplication(
		@RequestParam String email
	) {
		return ResponseEntity.ok(authService.isEmailAvailable(email));
	}

	@GetMapping("/signup/nickname/validation")
	public ResponseEntity<NicknameAvailableResponse> checkNicknameDuplication(
		@RequestParam String nickname
	) {
		return ResponseEntity.ok(authService.isNicknameAvailable(nickname));
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Void>> signup(
		@Valid @RequestBody SignupRequest signupRequest
	) {
		authService.signup(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED.value()).build();
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
		@RequestBody LoginRequest loginRequest
	) {
		Map<String, String> resultMap = authService.login(loginRequest);
		ResponseCookie responseCooke = createResponseCooke(resultMap.get("refreshToken"), Duration.ofDays(7));

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, responseCooke.toString())
			.body(LoginResponse.createOf(resultMap.get("nickname"), resultMap.get("accessToken")));
	}

	private ResponseCookie createResponseCooke(
		String value,
		Duration maxAge
	) {
		return ResponseCookie.from("refreshToken", value)
			.httpOnly(true)
			.secure(false)
			.path("/")
			.maxAge(maxAge)
			.build();
	}
}
