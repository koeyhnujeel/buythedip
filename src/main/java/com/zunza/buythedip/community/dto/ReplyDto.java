package com.zunza.buythedip.community.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyDto {
	private Long id;
	private Long parentId;
	private String author;
	private String content;
	private long likeCount;
	private boolean isLiked;
	private LocalDateTime createdAt;
}
