package com.zunza.buythedip.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.community.service.PostLikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

	private final PostLikeService postLikeService;

	@PostMapping("/api/posts/{postId}/likes")
	public ResponseEntity<Void> likePost(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long postId
	) {
		postLikeService.like(userId, postId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/api/posts/{postId}/likes")
	public ResponseEntity<Void> unlikePost(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long postId
	) {
		postLikeService.unlike(userId, postId);
		return ResponseEntity.ok().build();
	}
}
