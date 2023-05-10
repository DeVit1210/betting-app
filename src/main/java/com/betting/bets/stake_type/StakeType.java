package com.betting.bets.stake_type;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.impl.YesNoStakeType;
import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.event.Event;
import com.betting.events.sport.Sport;
import com.betting.results.type.ResultType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class StakeType implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, name = "stake_type_name")
    private String name;
    @OneToMany(mappedBy = "stakeType")
    private List<Stake> stakes;
    @ManyToMany
    @JoinTable(
            name = "sport_stake_types",
            joinColumns = @JoinColumn(name = "sport_id"),
            inverseJoinColumns = @JoinColumn(name = "stake_type_id")
    )
    private List<Sport> sports;
    private Class<?> clazz;
    private ResultType resultType;
    @ElementCollection
    @CollectionTable(name = "possibleBetNames", joinColumns = @JoinColumn(name = "stake_type_id"))
    @Column(name = "betName")
    protected List<String> possibleBetNames;
    public List<Stake> generateStakes(Event event) throws Exception {
        List<Stake> stakes = new ArrayList<>();
        for(String name : possibleBetNames) {
            Stake stake = (Stake) clazz.getDeclaredConstructor().newInstance();
            stakes.add(stake.build(name, this, resultType));
        }
        stakes.forEach(stake -> stake.setEvent(event));
        return stakes;
    }

    public StakeType(StakeTypeSpec stakeTypeSpec) {
        this.name = stakeTypeSpec.getName();
        this.sports = stakeTypeSpec.getSports();
        this.stakes = stakeTypeSpec.getStakes();
        try {
            this.clazz = Class.forName("com.betting.bets.stake.impl." + stakeTypeSpec.getClassTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.resultType = ResultType.valueOf(stakeTypeSpec.getResultTypeName());
        setPossibleNames(stakeTypeSpec.getPossibleBetNames());
    }
    public StakeType(String name, List<Stake> stakes,
                     List<Sport> sports, String className,
                     String resultTypeName, @Nullable List<String> possibleBetNames) {
        this.name = name;
        this.stakes = stakes;
        this.sports = sports;
        try {
            this.clazz = Class.forName("com.betting.bets.stake.impl." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.resultType = ResultType.valueOf(resultTypeName);
        setPossibleNames(possibleBetNames);
    }

    public abstract void setPossibleNames(@Nullable List<String> possibleBetNames);
}
