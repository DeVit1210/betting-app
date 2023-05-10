package com.betting.events.event;

import com.betting.events.betting_entity.BettingEntity;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record EventDto(Long id, String name, String firstOpponentName, String secondOpponentName, LocalDateTime time)
        implements BettingEntity { }
