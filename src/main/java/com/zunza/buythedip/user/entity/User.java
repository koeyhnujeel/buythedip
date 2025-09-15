package com.zunza.buythedip.user.entity;

import com.zunza.buythedip.auth.oauth2.dto.OAuth2Response;
import com.zunza.buythedip.common.BaseEntity;
import com.zunza.buythedip.user.constant.OAuth2Provider;
import com.zunza.buythedip.user.constant.UserRole;
import com.zunza.buythedip.user.constant.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Column
	@Enumerated(EnumType.STRING)
	private OAuth2Provider oAuth2Provider;

	@Column
	private String oAuth2ProviderId;

	@Builder
	private User(String email, String password, String nickname, UserRole userRole, UserType userType,
		OAuth2Provider oAuth2Provider, String oAuth2ProviderId) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.userRole = userRole;
		this.userType = userType;
		this.oAuth2Provider = oAuth2Provider;
		this.oAuth2ProviderId = oAuth2ProviderId;
	}

	public static User createNormalUser(
		String email,
		String password,
		String nickname
	) {
		return User.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.userRole(UserRole.USER)
			.userType(UserType.NORMAL)
			.oAuth2Provider(null)
			.oAuth2ProviderId(null)
			.build();
	}

	public static User createSocialUser(
		OAuth2Response oAuth2Response,
		String randomNickname
	) {
		return User.builder()
			.email(oAuth2Response.getEmail())
			.password(null)
			.nickname(randomNickname)
			.userRole(UserRole.USER)
			.userType(UserType.SOCIAL)
			.oAuth2Provider(oAuth2Response.getProvider())
			.oAuth2ProviderId(oAuth2Response.getProviderId())
			.build();
	}
}
