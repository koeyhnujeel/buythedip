package com.zunza.buythedip.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.common.ApiResponse;
import com.zunza.buythedip.user.dto.EmailAvailableResponse;
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
}
