package com.zunza.buythedip.watchlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.watchlist.entity.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
}
