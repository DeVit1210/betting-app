package com.betting.events.tournament;

import com.betting.events.country.Country;
import com.betting.events.sport.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Streamable<Tournament> findTournamentByCountry(Country country);
}
