package com.betting.events.sport;

import com.betting.bets.stake_type.StakeType;
import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.country.Country;
import com.betting.results.combinator.ScoreCombinatorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Sport implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "sport_name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "sport")
    private List<Country> countries;
    private boolean top;
    private int eventsQuantity;
    @Enumerated(EnumType.STRING)
    private ScoreCombinatorType combinatorType;
    @Enumerated(EnumType.STRING)
    private SportType sportType;
    @ManyToMany(mappedBy = "sports")
    private List<StakeType> stakeTypes;
    public Sport(String name, List<Country> countries, boolean top) {
        this.name = name;
        this.countries = countries;
        this.top = top;
    }
}
