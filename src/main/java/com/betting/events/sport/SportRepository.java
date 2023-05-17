package com.betting.events.sport;

import com.betting.results.combinator.ScoreCombinatorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport,Integer> {
    Streamable<Sport> findAllSportByTopIsTrueOrderByNameAsc();

    @Query("select s.combinatorType from Sport s where s.id = ?1")
    ScoreCombinatorType getCombinatorTypeById(Integer sportId);
}