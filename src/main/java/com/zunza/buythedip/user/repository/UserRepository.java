package com.zunza.buythedip.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByAccountId(String accountId);
	boolean existsByNickname(String nickname);
	Optional<User> findByAccountId(String accountId);
}
