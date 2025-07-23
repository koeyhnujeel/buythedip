package com.zunza.buythedip.community.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.community.entity.Comment;
import com.zunza.buythedip.community.entity.CommentLike;
import com.zunza.buythedip.community.entity.PostLike;
import com.zunza.buythedip.community.exception.CommentLikeNotFoundException;
import com.zunza.buythedip.community.exception.CommentNotFoundException;
import com.zunza.buythedip.community.exception.PostLikeNotFoundException;
import com.zunza.buythedip.community.repository.comment.CommentRepository;
import com.zunza.buythedip.community.repository.commentlike.CommentLikeRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final CommentLikeRepository commentLikeRepository;

	public void like(Long userId, Long commentId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentNotFoundException(commentId));

		commentLikeRepository.save(CommentLike.of(comment, user));
	}

	public void unlike(Long userId, Long commentId) {
		CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
			.orElseThrow(() -> new CommentLikeNotFoundException(commentId));

		commentLikeRepository.delete(commentLike);
	}
}
