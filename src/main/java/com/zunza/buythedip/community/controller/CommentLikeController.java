package com.zunza.buythedip.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.community.service.CommentLikeService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentLikeController {

	private final CommentLikeService commentLikeService;

	@PostMapping("/api/comments/{commentId}/likes")
	public ResponseEntity<Void> likeComment(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long commentId
	) {
		commentLikeService.like(userId, commentId);
		return ResponseEntity.status(HttpServletResponse.SC_CREATED).build();
	}

	@DeleteMapping("/api/comments/{commentId}/likes")
	public ResponseEntity<Void> unlikeComment(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long commentId
	) {
		commentLikeService.unlike(userId, commentId);
		return ResponseEntity.status(HttpServletResponse.SC_NO_CONTENT).build();
	}
}
