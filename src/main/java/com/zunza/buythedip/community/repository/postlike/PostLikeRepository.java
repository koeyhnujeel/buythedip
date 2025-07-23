package com.zunza.buythedip.community.repository.postlike;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.community.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
}
