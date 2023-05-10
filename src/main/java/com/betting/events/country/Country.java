package com.betting.events.country;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.sport.Sport;
import com.betting.events.tournament.Tournament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Country implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;
    @OneToMany(mappedBy = "country")
    private List<Tournament> tournaments;
}
