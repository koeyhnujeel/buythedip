package com.zunza.buythedip.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.news.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
