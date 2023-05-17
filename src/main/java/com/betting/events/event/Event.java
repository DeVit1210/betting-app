package com.betting.events.event;

import com.betting.bets.stake.Stake;
import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.tournament.Tournament;
import com.betting.results.EventResults;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sport_events")
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
    @OneToOne
    @JoinTable(name = "results",
            joinColumns = @JoinColumn(name="event_id"),
            inverseJoinColumns = @JoinColumn(name = "result_id"))
    private EventResults results;

    @OneToMany(mappedBy = "event")
    private List<Stake> stakes;
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

    public void setLive() {
        if(startTime.isBefore(LocalDateTime.now()) && !ended) {
            this.live = true;
        }
    }
}
