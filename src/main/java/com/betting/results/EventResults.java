package com.betting.results;

import com.betting.events.event.Event;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class EventResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "result_type")
    @Column(name = "quantity")
    private Map<ResultType, Integer> eventSummary = new HashMap<>();
    @OneToOne(mappedBy = "results")
    private Event event;
    private static final String FIRST_OPPONENT = "FIRST_OPPONENT_";
    private static final String SECOND_OPPONENT = "SECOND_OPPONENT_";
    // TODO: check result type
    public void addSummary(ResultType resultType, ResultPair resultPair) {
        String firstOpponentResultTypeName = FIRST_OPPONENT + resultType.name();
        String secondOpponentResultTypeName = SECOND_OPPONENT + resultType.name();
        addSummary(firstOpponentResultTypeName,secondOpponentResultTypeName, resultPair);
    }
    public void addSummary(ResultType resultType, Integer value) {
        eventSummary.put(resultType, value);
    }
    private void addSummary(String firstOpponentResultTypeName,
                            String secondOpponentResultTypeName,
                            ResultPair resultPair) {
        eventSummary.put(ResultType.valueOf(firstOpponentResultTypeName), resultPair.getFirstOpponentResult());
        eventSummary.put(ResultType.valueOf(secondOpponentResultTypeName), resultPair.getSecondOpponentResult());
    }
    public Integer getSummary(ResultType resultType) {
        return this.eventSummary.get(resultType);
    }
    public ResultPair getBothOpponentsSummary(ResultType resultType) {
        int firstOpponentResult = eventSummary.get(ResultType.valueOf(FIRST_OPPONENT + resultType.name()));
        int secondOpponentResult = eventSummary.get(ResultType.valueOf(SECOND_OPPONENT + resultType.name()));
        return ResultPair.of(firstOpponentResult, secondOpponentResult);
    }
    public String getResultsStringFormat(ResultType resultType) {
        return resultType.name() + " - " + eventSummary.get(resultType);
    }

    public ResultPair getOpponentsPoints() {
        return ResultPair.of(eventSummary.get(ResultType.FIRST_OPPONENT_POINTS), eventSummary.get(ResultType.SECOND_OPPONENT_POINTS));
    }
    public abstract Outcome outcome();

}
