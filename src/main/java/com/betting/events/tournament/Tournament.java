package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.country.Country;
import com.betting.events.event.Event;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tournaments")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Tournament implements BettingEntity {
    // TODO: maybe it makes sense to create more complex id generation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tournamentName;
    @ManyToOne
    private Country country;
    @OneToMany(mappedBy = "tournament")
    private List<Event> events;
}
