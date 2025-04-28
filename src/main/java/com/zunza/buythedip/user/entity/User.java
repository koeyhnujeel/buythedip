package com.zunza.buythedip.user.entity;

import com.zunza.buythedip.user.dto.SignupRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String accountId;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Builder
	private User(String accountId, String password, String nickname) {
		this.accountId = accountId;
		this.password = password;
		this.nickname = nickname;
	}

	public static User of(SignupRequestDto signupRequestDto, String encodedPassword) {
		return User.builder()
			.accountId(signupRequestDto.getAccountId())
			.password(encodedPassword)
			.nickname(signupRequestDto.getNickname())
			.build();
	}
}
