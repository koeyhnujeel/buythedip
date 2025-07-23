package com.zunza.buythedip.community.repository.comment;

import java.util.List;

import com.zunza.buythedip.community.dto.ReplyDto;
import com.zunza.buythedip.community.dto.CommentResponseDto;

public interface QuerydslCommentRepository {

	List<CommentResponseDto> findCommentsByPostId(Long userId, Long postId);

	List<ReplyDto> findRepliesByParentIds(Long userId, List<Long> ids);
}
