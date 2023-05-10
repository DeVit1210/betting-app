package com.betting.results.combinator;

import com.betting.results.ResultPair;

import java.util.List;

public class FightScoreCombinator implements ScoreCombinator {
    @Override
    public ResultPair countScore(List<ResultPair> periodScores) {
        int firstRoundsWonQuantity = 0, secondRoundsWonQuantity = 0;
        for(ResultPair periodScore : periodScores) {
            if(periodScore.getFirstOpponentResult() > periodScore.getSecondOpponentResult()) {
                firstRoundsWonQuantity++;
            } else if (periodScore.getFirstOpponentResult() < periodScore.getSecondOpponentResult()) {
                secondRoundsWonQuantity++;
            }
        }
        return ResultPair.of(firstRoundsWonQuantity, secondRoundsWonQuantity);
    }
}
