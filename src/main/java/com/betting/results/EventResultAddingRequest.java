package com.betting.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class EventResultAddingRequest {
    private Map<String, String> eventResults;
}
