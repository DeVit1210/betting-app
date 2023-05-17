package com.betting.events.sport;

import lombok.Builder;

@Builder
public record SportRequest(String name, boolean top, String combinatorType, String sportType) {
}
