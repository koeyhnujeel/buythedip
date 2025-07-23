package com.zunza.buythedip.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.community.dto.CreateCommentDto;
import com.zunza.buythedip.community.dto.CreateReplyDto;
import com.zunza.buythedip.community.dto.ModifyCommentRequestDto;
import com.zunza.buythedip.community.service.CommentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/api/posts/{postId}/comments")
	public ResponseEntity<Void> crateComment(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long postId,
		@RequestBody CreateCommentDto createCommentDto
	) {
		commentService.createComment(userId, postId, createCommentDto);
		return ResponseEntity.status(HttpServletResponse.SC_CREATED).build();
	}

	@PostMapping("/api/posts/{postId}/comments/{commentId}/replies")
	public ResponseEntity<Void> createReply(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long postId,
		@PathVariable Long commentId,
		@RequestBody CreateReplyDto createReplyDto
	) {
		commentService.createReply(userId, postId, commentId, createReplyDto);
		return ResponseEntity.status(HttpServletResponse.SC_CREATED).build();
	}

	@PatchMapping("/api/comments/{commentId}")
	public ResponseEntity<Void> modifyComment(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long commentId,
		@RequestBody ModifyCommentRequestDto modifyCommentRequestDto
	) {
		commentService.modifyComment(userId, commentId, modifyCommentRequestDto);
		return ResponseEntity.status(HttpServletResponse.SC_NO_CONTENT).build();
	}

	@DeleteMapping("/api/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long commentId
	) {
		commentService.deleteComment(userId, commentId);
		return ResponseEntity.status(HttpServletResponse.SC_NO_CONTENT).build();
	}
}
