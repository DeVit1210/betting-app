package com.betting.test_builder.impl;

import com.betting.bets.stake_type.StakeType;
import com.betting.events.country.Country;
import com.betting.events.sport.Sport;
import com.betting.events.sport.SportType;
import com.betting.results.combinator.ScoreCombinator;
import com.betting.results.combinator.ScoreCombinatorType;
import com.betting.test_builder.TestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(staticName = "aSport")
@AllArgsConstructor
@With
public class SportBuilder implements TestBuilder<Sport> {
    private String name = "sport";
    private List<Country> countries = Collections.emptyList();
    private boolean top = false;
    private int eventQuantity = 0;
    private ScoreCombinatorType scoreCombinatorType = ScoreCombinatorType.EMPTY;
    private SportType sportType = SportType.PLAYING_SPORT;
    private List<StakeType> stakeTypes = Collections.emptyList();
    @Override
    public Sport build() {
        Sport sport = new Sport();
        sport.setName(name);
        sport.setCountries(countries);
        sport.setTop(top);
        sport.setEventsQuantity(eventQuantity);
        sport.setCombinatorType(scoreCombinatorType);
        sport.setSportType(sportType);
        sport.setStakeTypes(stakeTypes);
        return sport;
    }
}
