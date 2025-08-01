package com.zunza.buythedip.community.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.zunza.buythedip.community.dto.ModifyPostRequestDto;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Lob
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cryptocurrency_id", nullable = false)
	private Cryptocurrency cryptocurrency;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User author;

	@Column(nullable = false)
	private int viewCount = 0;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostLike> likes = new ArrayList<>();

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Builder
	private Post(String title, String content, Cryptocurrency cryptocurrency, User author) {
		this.title = title;
		this.content = content;
		this.cryptocurrency = cryptocurrency;
		this.author = author;
	}

	public static Post of(String title, String content, Cryptocurrency cryptocurrency, User author) {
		return Post.builder()
			.title(title)
			.content(content)
			.cryptocurrency(cryptocurrency)
			.author(author)
			.build();
	}

	public void modifyPost(ModifyPostRequestDto modifyPostRequestDto) {
		this.title = (modifyPostRequestDto.getTitle() != null) ? modifyPostRequestDto.getTitle() : this.title;
		this.content = (modifyPostRequestDto.getContent() != null) ? modifyPostRequestDto.getContent() : this.content;
	}

	private Post(String title, String content, Cryptocurrency cryptocurrency, User author, int viewCount, LocalDateTime createdAt) {
		this.title = title;
		this.content = content;
		this.cryptocurrency = cryptocurrency;
		this.author = author;
		this.viewCount = viewCount;
		this.createdAt = createdAt;
	}

	public static Post createTestData(String title, String content, Cryptocurrency cryptocurrency, User author, int viewCount, LocalDateTime createdAt) {
		return new Post(
			title,
			content,
			cryptocurrency,
			author,
			viewCount,
			createdAt
		);
	}
}
