package com.betting.results.combinator;

import com.betting.results.ResultPair;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface ScoreCombinator {
    ResultPair countScore(List<ResultPair> periodScores);
    static ScoreCombinator getInstance(ScoreCombinatorType scoreCombinatorType) {
        return switch(scoreCombinatorType){
            case POINT_BY_POINT -> new PointByPointCombinator();
            case SET_BY_SET -> new SetBySetCombinator();
            case FIGHT -> new FightScoreCombinator();
            case EMPTY -> new EmptyCombinator();
        };
    }
}
