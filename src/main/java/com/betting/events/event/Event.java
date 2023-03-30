package com.betting.events.event;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentService;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sport_events")
@Getter
@NoArgsConstructor
@ToString
public class Event implements BettingEntity {
    // TODO: create more complex id generation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "event_name")
    private String name;
    @ManyToOne
    private Tournament tournament;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private String firstOpponentName;
    @Column(nullable = false)
    private String secondOpponentName;
    private boolean top;
    @Transient
    private boolean live = false;
    private boolean ended = false;
    public void setLive() {
        if(startTime.isBefore(LocalDateTime.now()) && !ended) {
            this.live = true;
        }
    }
    public Event(String name, Tournament tournament,
                 String firstOpponentName, String secondOpponentName,
                 LocalDateTime startTime, boolean top) {
        this.name = name;
        this.tournament = tournament;
        this.firstOpponentName = firstOpponentName;
        this.secondOpponentName = secondOpponentName;
        this.startTime = startTime;
        this.top = top;
    }
    // TODO: add EventResults
}
