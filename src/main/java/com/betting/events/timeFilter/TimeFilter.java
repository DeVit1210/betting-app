package com.betting.events.timeFilter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class TimeFilter {
    @Id
    private Integer filterId;
    private Integer hoursQuantity;
}
