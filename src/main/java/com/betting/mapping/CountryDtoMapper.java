package com.betting.mapping;

import com.betting.events.country.Country;
import com.betting.events.country.CountryDto;
import org.springframework.stereotype.Component;

@Component
public class CountryDtoMapper implements ObjectMapper<Country, CountryDto> {
    @Override
    public CountryDto mapFrom(Country country) {
        return CountryDto.builder().name(country.getName()).sportId(country.getId()).build();
    }
}
