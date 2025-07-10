package com.zunza.buythedip.community.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.community.dto.CreatePostDto;
import com.zunza.buythedip.community.entity.Post;
import com.zunza.buythedip.community.repository.PostRepository;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.exception.CryptoCurrencyNotFoundException;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CryptocurrencyRepository cryptocurrencyRepository;

	public void create(Long userId, Long cryptoId, CreatePostDto createPostDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Cryptocurrency cryptocurrency = cryptocurrencyRepository.findById(cryptoId)
			.orElseThrow(() -> new CryptoCurrencyNotFoundException(cryptoId));

		Post post = Post.of(createPostDto.getTitle(), createPostDto.getContent(), cryptocurrency, user);
		postRepository.save(post);
	}
}
