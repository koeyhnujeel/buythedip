package com.zunza.buythedip.news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.news.entity.MinId;
import com.zunza.buythedip.news.service.Topic;

@Repository
public interface MinIdRepository extends JpaRepository<MinId, Topic> {
	Optional<MinId> findByTopic(Topic topic);
}
