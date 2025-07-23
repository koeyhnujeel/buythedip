package com.zunza.buythedip.community.repository.post;

import java.util.List;

import com.zunza.buythedip.community.constant.PostSortType;
import com.zunza.buythedip.community.dto.PostCursorDto;
import com.zunza.buythedip.community.dto.PostResponseDto;

public interface QuerydslPostRepository {
	List<PostResponseDto> findPostsByCursor(Long userId, Long cryptoId, PostSortType sort, PostCursorDto cursor);

	PostResponseDto findPostWithCountsById(Long userId, Long postId);
}
