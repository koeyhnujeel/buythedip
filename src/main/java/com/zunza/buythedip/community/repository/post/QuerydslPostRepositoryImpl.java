package com.zunza.buythedip.community.repository.post;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zunza.buythedip.community.constant.PostSortType;
import com.zunza.buythedip.community.dto.PostCursorDto;
import com.zunza.buythedip.community.dto.PostResponseDto;
import com.zunza.buythedip.community.entity.QComment;
import com.zunza.buythedip.community.entity.QPost;
import com.zunza.buythedip.community.entity.QPostLike;
import com.zunza.buythedip.user.entity.QUser;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslPostRepositoryImpl implements QuerydslPostRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QUser user = QUser.user;
	private final QPost post = QPost.post;
	private final QComment comment = QComment.comment;
	private final QPostLike postLike = QPostLike.postLike;
	private final QPostLike userLike = new QPostLike("userLike");

	private static final int LIMIT = 20;

	@Override
	public List<PostResponseDto> findPostsByCursor(
		Long userId, Long cryptoId, PostSortType sort, PostCursorDto cursor
	) {
		return jpaQueryFactory
			.select(Projections.constructor(
				PostResponseDto.class,
				post.author.nickname,
				post.id,
				post.title,
				post.content,
				post.viewCount,
				comment.id.countDistinct(),
				postLike.id.countDistinct(),
				Expressions.cases()
					.when(userLike.id.max().isNotNull()).then(true)
					.otherwise(false),
				post.createdAt
			))
			.from(post)
			.leftJoin(post.author, user)
			.leftJoin(post.comments, comment)
			.leftJoin(post.likes, postLike)
			.leftJoin(post.likes, userLike).on(userId == null ? Expressions.FALSE : userLike.user.id.eq(userId))
			.where(
				post.cryptocurrency.id.eq(cryptoId),
				getWhereCondition(sort, cursor)
			)
			.groupBy(post.author.nickname, post.id, post.title, post.content, post.viewCount, post.createdAt)
			.having(getHavingCondition(sort, cursor))
			.orderBy(getOrderSpecifiers(sort))
			.limit(LIMIT)
			.fetch();
	}

	@Override
	@Transactional
	public PostResponseDto findPostWithCountsById(Long userId, Long postId) {
		jpaQueryFactory.update(post)
			.set(post.viewCount, post.viewCount.add(1))
			.where(post.id.eq(postId))
			.execute();

		return jpaQueryFactory
			.select(Projections.constructor(
				PostResponseDto.class,
				post.author.nickname,
				post.id,
				post.title,
				post.content,
				post.viewCount,
				comment.id.countDistinct(),
				postLike.id.countDistinct(),
				Expressions.cases()
					.when(userLike.id.max().isNotNull()).then(true)
					.otherwise(false),
				post.createdAt
			))
			.from(post)
			.leftJoin(post.author, user)
			.leftJoin(post.comments, comment)
			.leftJoin(post.likes, postLike)
			.leftJoin(post.likes, userLike).on(userId == null ? Expressions.FALSE : userLike.user.id.eq(userId))
			.where(post.id.eq(postId))
			.groupBy(post.author.nickname, post.id, post.title, post.content, post.viewCount, post.createdAt)
			.fetchOne();
	}

	private BooleanExpression getWhereCondition(PostSortType sort, PostCursorDto cursor) {
		if (cursor.getLastPostId() == null) {
			return null;
		}

		switch (sort) {
			case VIEWS:
				return post.viewCount.lt(cursor.getViewCount())
					.or(post.viewCount.eq(cursor.getViewCount())
						.and(post.id.lt(cursor.getLastPostId()))
					);

			case LATEST:
				return post.createdAt.lt(cursor.getLastCreatedAt())
					.or(post.createdAt.eq(cursor.getLastCreatedAt())
						.and(post.id.lt(cursor.getLastPostId()))
					);

			default:
				return null;

		}
	}

	private BooleanExpression getHavingCondition(PostSortType sort, PostCursorDto cursor) {
		if (cursor.getLastPostId() == null) {
			return null;
		}

		if (sort != PostSortType.POPULAR) {
			return null;
		}

		return postLike.id.countDistinct().lt(cursor.getLikeCount())
			.or(postLike.id.countDistinct().eq(Long.valueOf(cursor.getLikeCount()))
				.and(post.id.lt(cursor.getLastPostId()))
			);
	}

	private OrderSpecifier<?>[] getOrderSpecifiers(PostSortType sort) {
		return switch (sort) {
			case POPULAR -> new OrderSpecifier[] {
				postLike.id.countDistinct().desc(),
				post.id.desc()
			};

			case VIEWS -> new OrderSpecifier[] {
				post.viewCount.desc(),
				post.id.desc()
			};

			case LATEST -> new OrderSpecifier[] {
				post.createdAt.desc(),
				post.id.desc()
			};
		};
	}
}

