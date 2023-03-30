package com.betting.events.exception;

import com.betting.events.betting_entity.BettingEntity;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(Class<? extends BettingEntity> entityType) {
        super(entityType.getSimpleName().toLowerCase() + " is not found");
    }
}
