package com.zunza.buythedip.community.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.community.dto.CreateCommentDto;
import com.zunza.buythedip.community.dto.CreateReplyDto;
import com.zunza.buythedip.community.dto.ModifyCommentRequestDto;
import com.zunza.buythedip.community.entity.Comment;
import com.zunza.buythedip.community.entity.Post;
import com.zunza.buythedip.community.exception.CommentNotFoundException;
import com.zunza.buythedip.community.exception.PostNotFoundException;
import com.zunza.buythedip.community.repository.comment.CommentRepository;
import com.zunza.buythedip.community.repository.post.PostRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	public void createComment(Long userId, Long postId, CreateCommentDto createCommentDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));

		commentRepository.save(Comment.createComment(createCommentDto.getContent(), user, post));
	}

	public void createReply(Long userId, Long postId, Long commentId, CreateReplyDto createReplyDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentNotFoundException(commentId));

		commentRepository.save(Comment.createReply(createReplyDto.getContent(), user, post, comment));
	}

	@Transactional
	public void modifyComment(Long userId, Long commentId, ModifyCommentRequestDto modifyCommentRequestDto) {
		Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
			.orElseThrow(() -> new CommentNotFoundException(commentId));

		comment.modifyContent(modifyCommentRequestDto);
	}

	public void deleteComment(Long userId, Long commentId) {
		Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
			.orElseThrow(() -> new CommentNotFoundException(commentId));

		commentRepository.delete(comment);
	}
}
