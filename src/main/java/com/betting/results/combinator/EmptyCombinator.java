package com.betting.results.combinator;

import com.betting.results.ResultPair;

import java.util.List;

public class EmptyCombinator implements ScoreCombinator {
    @Override
    public ResultPair countScore(List<ResultPair> periodScores) {
        throw new UnsupportedOperationException();
    }
}
