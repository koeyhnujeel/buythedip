package com.zunza.buythedip.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.DuplicateCheckType;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.DuplicateAccountIdException;
import com.zunza.buythedip.user.exception.DuplicateNicknameException;
import com.zunza.buythedip.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AuthService authService;

	@Test
	void 회원_가입_성공() {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("test12", "password1!", "tester");
		when(userRepository.existsByAccountId(signupRequestDto.getAccountId())).thenReturn(false);
		when(userRepository.existsByNickname(signupRequestDto.getNickname())).thenReturn(false);

		// when
		assertDoesNotThrow(() -> authService.signup(signupRequestDto));

		// then
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void 회원_가입_실패_accountId_중복() {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("test12", "password1!", "tester");
		when(userRepository.existsByAccountId(signupRequestDto.getAccountId())).thenReturn(true);

		// when - then
		assertThrows(DuplicateAccountIdException.class, () ->
			authService.signup(signupRequestDto)
		);
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void 회원_가입_실패_nickname_중복() {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("test12", "password1!", "tester");
		when(userRepository.existsByAccountId(signupRequestDto.getAccountId())).thenReturn(false);
		when(userRepository.existsByNickname(signupRequestDto.getNickname())).thenReturn(true);

		// when - then
		assertThrows(DuplicateNicknameException.class, () ->
			authService.signup(signupRequestDto)
		);
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void 계정ID_중복_확인_중복x() {
		// given
		DuplicateCheckType type = DuplicateCheckType.ACCOUNT_ID;
		String value = "test12";
		when(userRepository.existsByAccountId(value)).thenReturn(false);

		// when - then
		assertDoesNotThrow(() -> authService.validateDuplicateField(type, value));
		verify(userRepository).existsByAccountId(value);
	}

	@Test
	void 계정ID_중복_확인_중복o() {
		// given
		DuplicateCheckType type = DuplicateCheckType.ACCOUNT_ID;
		String value = "test12";
		when(userRepository.existsByAccountId(value)).thenReturn(true);

		// when - then
		assertThrows(DuplicateAccountIdException.class, () ->
			authService.validateDuplicateField(type, value));
		verify(userRepository).existsByAccountId(value);
	}

	@Test
	void 닉네임_중복_확인_중복x() {
		// given
		DuplicateCheckType type = DuplicateCheckType.NICKNAME;
		String value = "tester";
		when(userRepository.existsByNickname(value)).thenReturn(false);

		// when - then
		assertDoesNotThrow(() -> authService.validateDuplicateField(type, value));
		verify(userRepository).existsByNickname(value);
	}

	@Test
	void 닉네임_중복_확인_중복o() {
		// given
		DuplicateCheckType type = DuplicateCheckType.NICKNAME;
		String value = "tester";
		when(userRepository.existsByNickname(value)).thenReturn(true);

		// when - then
		assertThrows(DuplicateNicknameException.class, () ->
			authService.validateDuplicateField(type, value));
		verify(userRepository).existsByNickname(value);
	}
}
