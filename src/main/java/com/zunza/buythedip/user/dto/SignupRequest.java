package com.zunza.buythedip.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {
	@NotBlank(message = "이메일을 입력해 주세요.")
	@Pattern(
		regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
		message = "잘못된 이메일 형식입니다."
	)
	private String email;

	@NotBlank(message = "비밀번호를 입력해 주세요.")
	@Size(min = 8, max = 24, message = "비밀번호는 8자 이상 24자 이하로 입력해주세요.")
	@Pattern(
		regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
		message = "비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 최소 1개 이상 포함해야 합니다."
	)
	private String password;

	@NotBlank(message = "닉네임을 입력해 주세요.")
	@Size(min = 2, max = 12, message = "닉네임은 2자 이상 12자 이하로 입력해주세요.")
	@Pattern(
		regexp = "^(?=.*[가-힣a-zA-Z])[가-힣a-zA-Z0-9]{2,12}$",
		message = "닉네임은 한글, 영문자, 숫자 조합만 사용 가능합니다."
	)
	private String nickname;
}
