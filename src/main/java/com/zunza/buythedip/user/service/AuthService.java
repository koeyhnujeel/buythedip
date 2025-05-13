package com.zunza.buythedip.user.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.security.CustomUserDetails;
import com.zunza.buythedip.security.JwtTokenProvider;
import com.zunza.buythedip.security.RefreshTokenService;
import com.zunza.buythedip.security.util.TokenUtil;
import com.zunza.buythedip.user.dto.LoginRequestDto;
import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.DuplicateCheckType;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.DuplicateAccountIdException;
import com.zunza.buythedip.user.exception.DuplicateNicknameException;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;

	public void signup(SignupRequestDto signupRequestDto) {
		if (userRepository.existsByAccountId(signupRequestDto.getAccountId())) {
			throw new DuplicateAccountIdException();
		}

		if (userRepository.existsByNickname(signupRequestDto.getNickname())) {
			throw new DuplicateNicknameException();
		}

		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
		userRepository.save(User.of(signupRequestDto,encodedPassword));
	}

	public void validateDuplicateField(DuplicateCheckType type, String value) {
		switch (type) {
			case ACCOUNT_ID -> {
				if (userRepository.existsByAccountId(value)) {
					throw new DuplicateAccountIdException();
				}
			}

			case NICKNAME -> {
				if (userRepository.existsByNickname(value)) {
					throw new DuplicateNicknameException();
				}
			}

			default -> throw new IllegalArgumentException("지원하지 않는 필드 타입입니다.");
		}
	}

	public Map<String, String> login(LoginRequestDto loginRequestDto) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			loginRequestDto.getAccountId(), loginRequestDto.getPassword());

		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		String accessToken = jwtTokenProvider.generateAccessToken(authenticate);
		String refreshToken = jwtTokenProvider.generateRefreshToken(authenticate);

		CustomUserDetails principal = (CustomUserDetails)authenticate.getPrincipal();
		refreshTokenService.saveRefreshToken(principal.getUserId(), refreshToken);

		return Map.of(
			"nickname", principal.getNickname(),
			"accessToken", accessToken,
			"refreshToken", refreshToken
		);
	}

	public Map<String, String> reissue(String authorizationHeader, String refreshToken) {
		String expiredAccessToken = TokenUtil.resolveToken(authorizationHeader);
		Claims claims = jwtTokenProvider.parseClaims(expiredAccessToken);
		Long userId = claims.get("userId", Long.class);

		jwtTokenProvider.validateToken(refreshToken);
		if (!refreshTokenService.validateRefreshToken(userId, refreshToken)) {
			throw new IllegalArgumentException("리프레시 토큰 불일치");
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());

		String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
		String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

		refreshTokenService.deleteRefreshToken(userId);
		refreshTokenService.saveRefreshToken(userId, newRefreshToken);

		return Map.of(
			"newAccessToken", newAccessToken,
			"newRefreshToken", newRefreshToken
		);
	}
}
