package com.betting.events.event;

import lombok.Builder;
import lombok.Getter;

public record EventAddingRequest(String name, String firstOpponentName, String secondOpponentName, String localDateTime, boolean top) { }
