package com.betting.security.auth.mapping;

import org.springframework.stereotype.Component;
@Component
public interface ObjectMapper<T, R> {
    R mapFrom(T obj);
}
