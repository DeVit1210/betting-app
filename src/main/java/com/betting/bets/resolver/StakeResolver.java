package com.betting.bets.resolver;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.results.EventResults;

public interface StakeResolver {
    void resolveStake(Stake stake, EventResults eventResults);
}
