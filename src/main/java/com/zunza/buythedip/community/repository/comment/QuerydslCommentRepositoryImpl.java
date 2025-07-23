package com.zunza.buythedip.community.repository.comment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zunza.buythedip.community.dto.ReplyDto;
import com.zunza.buythedip.community.dto.CommentResponseDto;
import com.zunza.buythedip.community.entity.QComment;
import com.zunza.buythedip.community.entity.QCommentLike;
import com.zunza.buythedip.user.entity.QUser;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuerydslCommentRepositoryImpl implements QuerydslCommentRepository{

	private final JPAQueryFactory jpaQueryFactory;
	private final QUser user = QUser.user;
	private final QComment comment = QComment.comment;
	private final QCommentLike commentLike = QCommentLike.commentLike;
	private final QCommentLike userCommentLike = new QCommentLike("userCommentLike");

	@Override
	public List<CommentResponseDto> findCommentsByPostId(Long userId, Long postId) {
		return jpaQueryFactory
			.select(Projections.constructor(
				CommentResponseDto.class,
				comment.id,
				comment.author.nickname,
				comment.content,
				commentLike.id.countDistinct(),
				Expressions.cases()
					.when(userCommentLike.id.max().isNotNull()).then(true)
					.otherwise(false),
				comment.createdAt
			))
			.from(comment)
			.leftJoin(comment.author, user)
			.leftJoin(comment.likes, commentLike)
			.leftJoin(comment.likes, userCommentLike).on(userId == null ? Expressions.FALSE : userCommentLike.user.id.eq(userId))
			.where(comment.post.id.eq(postId).and(comment.parent.isNull()))
			.groupBy(comment.id, comment.author.nickname, comment.content, comment.createdAt)
			.orderBy(comment.createdAt.asc())
			.fetch();
	}

	@Override
	public List<ReplyDto> findRepliesByParentIds(Long userId, List<Long> ids) {
		return jpaQueryFactory
			.select(Projections.constructor(
				ReplyDto.class,
				comment.id,
				comment.parent.id,
				comment.author.nickname,
				comment.content,
				commentLike.id.countDistinct(),
				Expressions.cases()
					.when(userCommentLike.id.max().isNotNull()).then(true)
					.otherwise(false),
				comment.createdAt
			))
			.from(comment)
			.leftJoin(comment.author, user)
			.leftJoin(comment.likes, commentLike)
			.leftJoin(comment.likes, userCommentLike).on(userId == null ? Expressions.FALSE : userCommentLike.user.id.eq(userId))
			.where(comment.parent.id.in(ids))
			.groupBy(comment.id, comment.parent.id, comment.author.nickname, comment.content, comment.createdAt)
			.orderBy(comment.createdAt.asc())
			.fetch();
	}
}
