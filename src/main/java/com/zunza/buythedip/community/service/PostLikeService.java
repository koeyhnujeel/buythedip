package com.zunza.buythedip.community.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.community.entity.Post;
import com.zunza.buythedip.community.entity.PostLike;
import com.zunza.buythedip.community.exception.PostLikeNotFoundException;
import com.zunza.buythedip.community.exception.PostNotFoundException;
import com.zunza.buythedip.community.repository.postlike.PostLikeRepository;
import com.zunza.buythedip.community.repository.post.PostRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	public void like(Long userId, Long postId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));

		postLikeRepository.save(PostLike.of(post, user));
	}

	public void unlike(Long userId, Long postId) {
		PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
			.orElseThrow(() -> new PostLikeNotFoundException(postId));

		postLikeRepository.delete(postLike);
	}
}
