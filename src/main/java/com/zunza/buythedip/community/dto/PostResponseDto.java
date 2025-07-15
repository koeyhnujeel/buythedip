package com.zunza.buythedip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
	private Long postId;
	private String title;
	private String content;
	private int viewCount;
	private long commentCount;
	private long likeCount;
	private boolean isLiked;
}
