package com.betting.bets.stake_type;

import com.betting.events.sport.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StakeTypeRepository extends JpaRepository<StakeType, Integer> {
    // TODO: check this method
    @Query("select s from StakeType s join s.sports sp where sp.id=?1")
    List<StakeType> findStakeTypesBySport(Integer sportId);
}
