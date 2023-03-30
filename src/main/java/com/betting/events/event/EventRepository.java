package com.betting.events.event;

import com.betting.events.tournament.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Streamable<Event> findAllByTopIsTrueOrderByNameAsc();
    Streamable<Event> findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(Tournament tournament, LocalDateTime time);
    List<Event> findAllByStartTimeAfter(LocalDateTime time);
}
