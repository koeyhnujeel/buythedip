package com.zunza.buythedip.community.repository.comment;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.community.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslCommentRepository {
	Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);
}
