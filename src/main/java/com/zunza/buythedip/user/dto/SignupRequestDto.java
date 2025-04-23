package com.zunza.buythedip.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
	private String accountId;
	private String password;
	private String nickname;
}
