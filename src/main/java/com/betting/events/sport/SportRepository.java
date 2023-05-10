package com.betting.events.sport;

import com.betting.results.combinator.ScoreCombinatorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportRepository extends JpaRepository<Sport,Integer> {
    List<Sport> findAllSportByTopIsTrueOrderByNameAsc();
    @Query("select s.combinatorType from Sport s where s.id = ?1")
    ScoreCombinatorType getCombinatorTypeById(Integer sportId);
}