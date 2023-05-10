package com.betting.bets.resolver;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.results.EventResults;

import static com.betting.bets.stake.StakeOutcome.LOSE;
import static com.betting.bets.stake.StakeOutcome.WIN;

public class OutcomeResolver implements StakeResolver {
    @Override
    public void resolveStake(Stake stake, EventResults eventResults) {
        stake.setStakeOutcome(switch (eventResults.outcome()) {
            case FIRST_WIN -> stake.getName().equals("W1") ? WIN : LOSE;
            case DRAW -> stake.getName().equals("X") ? WIN : LOSE;
            case SECOND_WIN -> stake.getName().equals("W2") ? WIN : LOSE;
        });
    }
}
