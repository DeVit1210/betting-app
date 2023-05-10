package com.betting.bets.stake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StakeAddingRequest {
    private Map<String, Float> stakeNamesAndFactors;
}
