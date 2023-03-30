package com.betting.events.timeFilter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeFilterRepository extends JpaRepository<TimeFilter, Integer> {
}
