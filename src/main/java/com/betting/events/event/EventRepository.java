package com.betting.events.event;

import com.betting.events.tournament.Tournament;
import com.betting.results.EventResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Streamable<Event> findAllByTopIsTrueOrderByNameAsc();
    Streamable<Event> findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(Tournament tournament, LocalDateTime time);
    List<Event> findAllByStartTimeAfter(LocalDateTime time);
    @Transactional
    @Modifying
    @Query("update Event e set e.results = ?2 where e.id = ?1")
    void updateResults(Long eventId, EventResults results);
}
