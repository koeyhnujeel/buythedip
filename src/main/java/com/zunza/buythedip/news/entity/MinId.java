package com.zunza.buythedip.news.entity;

import com.zunza.buythedip.news.service.Topic;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MinId {

	@Id
	@Enumerated(EnumType.STRING)
	private Topic topic;

	private Long latestId;

	public MinId(Topic topic) {
		this.topic = topic;
		this.latestId = 0L;
	}

	public Topic getTopic() {
		return topic;
	}

	public Long getLatestId() {
		return latestId;
	}

	public void updateLatestId(Long newLatestId) {
		this.latestId = newLatestId;
	}
}
