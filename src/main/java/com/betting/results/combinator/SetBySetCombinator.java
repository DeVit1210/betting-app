package com.betting.results.combinator;

import com.betting.results.ResultPair;

import java.util.List;

public class SetBySetCombinator implements ScoreCombinator {
    @Override
    public ResultPair countScore(List<ResultPair> periodScores) {
        int firstSetsWon = periodScores.stream()
                .reduce(0, (midSum, resultPair) -> {
                    boolean firstWon = resultPair.getFirstOpponentResult() > resultPair.getSecondOpponentResult();
                    return firstWon ? midSum + 1 : midSum;
                }, Integer::sum);
        int secondSetsWon = periodScores.size() - firstSetsWon;
        return ResultPair.of(firstSetsWon, secondSetsWon);
    }
}
