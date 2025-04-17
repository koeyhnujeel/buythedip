package com.zunza.buythedip.news.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zunza.buythedip.news.dto.NewsListResponseDto;
import com.zunza.buythedip.news.entity.QNews;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuerydslNewsRepositoryImpl implements QuerydslNewsRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QNews news = QNews.news;

	@Override
	public List<NewsListResponseDto> findByCursor(Long cursor, int size) {
		return jpaQueryFactory
			.select(Projections.constructor(
				NewsListResponseDto.class,
				news.id,
				news.headline,
				news.datetime
			))
			.from(news)
			.where(cursorCondition(cursor))
			.orderBy(news.datetime.desc(), news.id.desc())
			.limit(size)
			.fetch();
	}

	private BooleanExpression cursorCondition(Long cursor) {
		if (cursor == null) {
			return null;
		}

		return news.datetime.lt(cursor);
	}
}
