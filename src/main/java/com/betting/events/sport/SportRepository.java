package com.betting.events.sport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportRepository extends JpaRepository<Sport,Integer> {
    List<Sport> findAllSportByTopIsTrueOrderByNameAsc();
}