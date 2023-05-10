package com.betting.bets.stake;

import com.betting.bets.stake_type.StakeType;
import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
public abstract class Stake implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "stake_name")
    private String name;
    @Column(nullable = false, name = "full_stake_name")
    private String fullStakeName;
    @Column
    private float factor;
    @Column(nullable = false)
    private boolean isExecuted = false;
    @Column(name = "result")
    protected StakeOutcome stakeOutcome;
    @ManyToOne
    @JoinColumn(name = "stake_type_id")
    private StakeType stakeType;
    @Enumerated(EnumType.STRING)
    private ResultType resultType;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    public abstract void resolveOutcome(EventResults eventResults);
    public Stake(String name, StakeType stakeType, ResultType resultType) {
        this.name = name;
        this.stakeType = stakeType;
        this.resultType = resultType;
        this.fullStakeName = this.stakeType.getName() + " " + this.name;
    }
    public abstract Stake build(String name, StakeType stakeType, ResultType resultType);
}


