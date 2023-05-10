package com.betting.results;

import com.betting.results.type.Outcome;
import jakarta.persistence.Entity;

import static com.betting.results.type.Outcome.*;
@Entity
public class FightEventResults extends EventResults {
    @Override
    public Outcome outcome() {
        if(getOpponentsPoints().getFirstOpponentResult() > 1) return FIRST_WIN;
        if(getOpponentsPoints().getSecondOpponentResult() > 1) return SECOND_WIN;
        return DRAW;
    }
}
