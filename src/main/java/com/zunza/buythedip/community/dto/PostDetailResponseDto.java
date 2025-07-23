package com.zunza.buythedip.community.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDetailResponseDto {
	private PostResponseDto post;
	private List<CommentResponseDto> comments;

	@Builder
	private PostDetailResponseDto(PostResponseDto post, List<CommentResponseDto> comments) {
		this.post = post;
		this.comments = comments;
	}

	public static PostDetailResponseDto of(PostResponseDto post, List<CommentResponseDto> comments) {
		return PostDetailResponseDto.builder()
			.post(post)
			.comments(comments)
			.build();
	}
}
