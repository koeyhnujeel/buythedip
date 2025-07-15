package com.zunza.buythedip.community.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCursorDto {
	private Long lastPostId;
	private Integer likeCount;
	private Integer viewCount;
	private LocalDateTime lastCreatedAt;
}
