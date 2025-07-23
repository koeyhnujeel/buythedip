package com.zunza.buythedip.community.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.community.constant.PostSortType;
import com.zunza.buythedip.community.dto.ModifyPostRequestDto;
import com.zunza.buythedip.community.dto.ReplyDto;
import com.zunza.buythedip.community.dto.CreatePostDto;
import com.zunza.buythedip.community.dto.CommentResponseDto;
import com.zunza.buythedip.community.dto.PostCursorDto;
import com.zunza.buythedip.community.dto.PostDetailResponseDto;
import com.zunza.buythedip.community.dto.PostResponseDto;
import com.zunza.buythedip.community.entity.Post;
import com.zunza.buythedip.community.exception.PostNotFoundException;
import com.zunza.buythedip.community.repository.comment.CommentRepository;
import com.zunza.buythedip.community.repository.post.PostRepository;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.exception.CryptoCurrencyNotFoundException;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final CryptocurrencyRepository cryptocurrencyRepository;

	public void create(Long userId, Long cryptoId, CreatePostDto createPostDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Cryptocurrency cryptocurrency = cryptocurrencyRepository.findById(cryptoId)
			.orElseThrow(() -> new CryptoCurrencyNotFoundException(cryptoId));

		Post post = Post.of(createPostDto.getTitle(), createPostDto.getContent(), cryptocurrency, user);
		postRepository.save(post);
	}

	public List<PostResponseDto> getPostsByCursor(Long userId, Long cryptoId, PostSortType sort, PostCursorDto cursor) {
		return postRepository.findPostsByCursor(userId, cryptoId, sort, cursor);
	}

	public PostDetailResponseDto getPostWithComments(Long userId, Long postId) {
		PostResponseDto postResponseDto = postRepository.findPostWithCountsById(userId, postId);

		List<CommentResponseDto> parentComments = commentRepository.findCommentsByPostId(userId, postId);
		List<ReplyDto> replies = commentRepository.findRepliesByParentIds(userId,
			getParentIds(parentComments));

		Map<Long, List<ReplyDto>> replyMap = replies.stream()
			.collect(Collectors.groupingBy(ReplyDto::getParentId));

		parentComments.forEach(parent ->
			parent.setReplies(replyMap.get(parent.getId()))
		);

		return PostDetailResponseDto.of(postResponseDto, parentComments);
	}

	@Transactional
	public void modify(Long userId, Long postId, ModifyPostRequestDto modifyPostRequestDto) {
		Post post = postRepository.findByIdAndAuthorId(postId, userId)
			.orElseThrow(() -> new PostNotFoundException(postId));

		post.modifyPost(modifyPostRequestDto);
	}

	public void delete(Long userId, Long postId) {
		Post post = postRepository.findByIdAndAuthorId(postId, userId)
			.orElseThrow(() -> new PostNotFoundException(postId));

		postRepository.delete(post);
	}

	private List<Long> getParentIds(List<CommentResponseDto> parentComments) {
		return parentComments.stream()
			.map(CommentResponseDto::getId)
			.toList();
	}
}
