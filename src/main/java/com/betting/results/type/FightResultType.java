package com.betting.results.type;

import com.betting.exceptions.InvalidRequestParameterException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum FightResultType {
    FIRST_FIGHTER_DISQUALIFICATION(5, "W1/Disqualification"),
    FIRST_FIGHTER_DOCTOR_STOPPAGE(7, "W1/Doctor stoppage"),
    FIRST_FIGHTER_KNOCKOUT(1, "W1/Knockout"),
    FIRST_FIGHTER_SUBMISSION(3, "W1/Submission"),
    NO_CONTEST(9, "NC"),
    SECOND_FIGHTER_DISQUALIFICATION(6, "W2/Disqualification"),
    SECOND_FIGHTER_DOCTOR_STOPPAGE(8, "W2/Doctor stoppage"),
    SECOND_FIGHTER_KNOCKOUT(2, "W2/Knockout"),
    SECOND_FIGHTER_SUBMISSION(4, "W2/Submission"),
    DECISION(10, "Decision");
    private final int index;
    private final String stakeOutcomeName;
    public static FightResultType getByIndex(int index) {
        for (FightResultType type : values()) {
            if (type.index == index) {
                return type;
            }
        }
        throw new InvalidRequestParameterException("fight finish type index is invalid!");
    }
    public static FightResultType of(String stakeName) {
        return Arrays.stream(FightResultType.values())
                .filter(fightResultType -> fightResultType.getStakeOutcomeName().equals(stakeName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    public static FightResultType getAnyBut(FightResultType fightResultType) {
        return Arrays.stream(FightResultType.values())
                .filter(resultType -> !resultType.equals(fightResultType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
