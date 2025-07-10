package com.zunza.buythedip.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.community.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
