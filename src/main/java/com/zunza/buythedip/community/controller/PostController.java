package com.zunza.buythedip.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.community.dto.CreatePostDto;
import com.zunza.buythedip.community.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/api/cryptocurrencies/{id}/posts")
	public ResponseEntity<Void> createPost(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "id") Long cryptoId,
		@RequestBody CreatePostDto createPostDto
	) {
		postService.create(userId, cryptoId, createPostDto);
		return ResponseEntity.ok().build();
	}
}
