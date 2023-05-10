package com.betting.results;

import com.betting.results.type.Outcome;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import static com.betting.results.type.Outcome.*;
@Entity
public class PlayingSportResults extends EventResults {
    @Override
    public Outcome outcome() {
        int firstResults = getOpponentsPoints().getFirstOpponentResult();
        int secondResults = getOpponentsPoints().getSecondOpponentResult();
        return firstResults > secondResults ? FIRST_WIN : (firstResults < secondResults ? SECOND_WIN : DRAW);
    }
}
