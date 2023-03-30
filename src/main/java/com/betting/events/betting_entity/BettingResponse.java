package com.betting.events.betting_entity;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class BettingResponse {
    private List<? extends BettingEntity> entities;
}
