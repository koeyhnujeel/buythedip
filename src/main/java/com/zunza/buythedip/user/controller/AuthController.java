package com.zunza.buythedip.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.DuplicateCheckType;
import com.zunza.buythedip.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/api/auth/signup")
	public ResponseEntity<Void> signup(
		@RequestBody SignupRequestDto signupRequestDto
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
}
