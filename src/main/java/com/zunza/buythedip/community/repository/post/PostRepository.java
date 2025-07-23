package com.zunza.buythedip.community.repository.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.community.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPostRepository {
	Optional<Post> findByIdAndAuthorId(Long postId, Long userId);
}
