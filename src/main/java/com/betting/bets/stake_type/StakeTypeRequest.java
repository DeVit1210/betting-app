package com.betting.bets.stake_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class StakeTypeRequest {
    private String stakeTypeName;
    private String stakeTypeVariant;
    private String resultTypeName;
    private String classTypeName;
    private List<Integer> associatedSportIndexes;
    private List<String> possibleBetNames;
}
