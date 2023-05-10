package com.betting.bets.stake;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StakeRepository extends JpaRepository<Stake, Long> {
}
