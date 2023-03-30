package com.betting.events.sport;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.country.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "sport_type")
@NoArgsConstructor
@Getter
@Setter
public class Sport implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "sport_name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "sport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Country> countries;
    private boolean top;
    private int eventsQuantity;
    public Sport(String name, List<Country> countries, boolean top) {
        this.name = name;
        this.countries = countries;
        this.top = top;
    }
}
