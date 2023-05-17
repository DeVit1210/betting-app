package com.betting.mapping;

import org.springframework.stereotype.Component;
@Component
public interface ObjectMapper<T, R> {
    R mapFrom(T obj);
}
