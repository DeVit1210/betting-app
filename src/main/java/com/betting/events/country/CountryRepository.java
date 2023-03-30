package com.betting.events.country;

import com.betting.events.sport.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Streamable<Country> findAllCountriesBySport(Sport sport);
}
