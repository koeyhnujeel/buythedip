package com.zunza.buythedip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostDto {
	private String title;
	private String content;
}
