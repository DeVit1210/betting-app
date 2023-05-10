package com.betting.results;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Embeddable
public class ResultPair {
    private int firstOpponentResult;
    private int secondOpponentResult;
    public static ResultPair of(int firstOpponentResult, int secondOpponentResult) {
        return new ResultPair(firstOpponentResult, secondOpponentResult);
    }

    public int sum() {
        return this.firstOpponentResult + this.secondOpponentResult;
    }
}
