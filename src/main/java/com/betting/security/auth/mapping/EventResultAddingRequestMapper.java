package com.betting.security.auth.mapping;

import com.betting.results.EventResultAddingRequest;
import com.betting.results.EventResults;
import com.betting.results.EventResultsFactory;
import com.betting.results.ResultPair;
import com.betting.results.combinator.ScoreCombinator;
import com.betting.results.combinator.ScoreCombinatorType;
import com.betting.events.sport.SportType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.betting.results.type.ResultType.POINTS;
import static com.betting.results.type.ResultType.valueOf;
@Component("eventResultMapper")
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
public class EventResultAddingRequestMapper implements ObjectMapper<EventResultAddingRequest, EventResults> {
    private ScoreCombinatorType scoreCombinatorType;
    private SportType sportType;
    @Override
    public EventResults mapFrom(EventResultAddingRequest request) {
        Map<String, String> results = new HashMap<>();
        request.getEventResults()
                .forEach((key, value) -> results.put(key.replaceAll(" ", "_").toUpperCase(), value));
        EventResults eventResults = EventResultsFactory.createEventResults(sportType);
        results.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(":"))
                .forEach(entry -> eventResults.addSummary(valueOf(entry.getKey().toUpperCase()), getResultPair(entry.getValue())));
        results.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().contains(":"))
                .forEach(entry -> eventResults.addSummary(valueOf(entry.getKey().toUpperCase()), Integer.parseInt(entry.getValue())));
        if(!results.containsKey(POINTS.name())) {
            ScoreCombinator scoreCombinator = ScoreCombinator.getInstance(scoreCombinatorType);
            List<ResultPair> periodResults = results.entrySet().stream()
                    .filter(entry -> entry.getKey().contains("PERIOD"))
                    .map(entry -> getResultPair(entry.getValue()))
                    .toList();
            ResultPair score = scoreCombinator.countScore(periodResults);
            eventResults.addSummary(POINTS, score);
        }
        return eventResults;
    }

    private ResultPair getResultPair(String resultInStringFormat) {
        String[] bothOpponentScores = resultInStringFormat.split(":");
        int firstOppResult = Integer.parseInt(bothOpponentScores[0]);
        int secondOppResult = Integer.parseInt(bothOpponentScores[1]);
        return ResultPair.of(firstOppResult, secondOppResult);
    }
}