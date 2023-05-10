package com.betting.results;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventResultsRepository extends JpaRepository<EventResults, Long> {

}
