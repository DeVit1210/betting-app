package com.betting.events.event;

public record EventAddingRequest(String name, String firstOpponentName, String secondOpponentName, String localDateTime, boolean top) { }
