package com.betting.results.combinator;

import com.betting.results.ResultPair;

import java.util.List;

// football, hockey, basketball, handball
public class PointByPointCombinator implements ScoreCombinator {
    @Override
    public ResultPair countScore(List<ResultPair> periodScores) {
        int firstTeamPoints = periodScores.stream()
                .reduce(0, (midSum, resultPair) -> midSum + resultPair.getFirstOpponentResult(), Integer::sum);
        int secondTeamPoints = periodScores.stream()
                .reduce(0, (midSum, resultPair) -> midSum + resultPair.getSecondOpponentResult(), Integer::sum);
        return ResultPair.of(firstTeamPoints, secondTeamPoints);
    }
}