package com.betting.bets.stake;

import java.util.Arrays;

public enum StakeOutcome {
    WIN, LOSE, RETURN;

    public static StakeOutcome getAnyBut(StakeOutcome outcome) {
        return Arrays.stream(StakeOutcome.values()).filter(e -> !e.equals(outcome)).findFirst().orElseThrow();
    }
}
