package com.betting.bets.stake_type;

import com.betting.bets.stake.Stake;
import com.betting.events.sport.Sport;
import com.betting.results.type.ResultType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class StakeTypeSpec {
    private final String name;
    private final List<Stake> stakes;
    private final List<Sport> sports;
    private final String classTypeName;
    private final String resultTypeName;
    private final List<String> possibleBetNames;
}
