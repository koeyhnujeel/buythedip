package com.zunza.buythedip.community.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
	private Long id;
	private String author;
	private String content;
	private long likeCount;
	private boolean isLiked;
	private LocalDateTime createdAt;
	private List<ReplyDto> replies = new ArrayList<>();

	@QueryProjection
	public CommentResponseDto(Long id, String author, String content, long likeCount, boolean isLiked,
		LocalDateTime createdAt) {
		this.id = id;
		this.author = author;
		this.content = content;
		this.likeCount = likeCount;
		this.isLiked = isLiked;
		this.createdAt = createdAt;
	}

	public void setReplies(List<ReplyDto> childrenComments) {
		this.replies = childrenComments;
	}
}
