package com.betting.results.type;

import java.util.Arrays;

public enum Outcome {
    FIRST_WIN("W1"), DRAW("X"), SECOND_WIN("W2");
    private String name;

    Outcome(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Outcome getAnyBut(Outcome outcome) {
        return Arrays.stream(Outcome.values())
                .filter(currentOutcome -> !currentOutcome.name.equals(outcome.name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Outcome of(String name) {
        return Arrays.stream(Outcome.values())
                .filter(outcome -> outcome.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
