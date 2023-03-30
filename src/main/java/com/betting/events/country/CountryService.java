package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.exception.EntityNotFoundException;
import com.betting.events.exception.InvalidRequestParameterException;
import com.betting.events.sport.Sport;
import com.betting.events.sport.SportService;
import com.betting.events.timeFilter.TimeFilter;
import com.betting.events.timeFilter.TimeFilterRepository;
import com.betting.events.util.BettingEntityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final SportService sportService;
    private final BettingEntityFilter bettingEntityFilter;

    public BettingResponse getCountriesWithAvailableEvents(int sportId, int timeFilter) {
        Sport sport = sportService.getSport(sportId);
        Streamable<Country> allCountries = countryRepository.findAllCountriesBySport(sport);
        List<Country> countriesWithAvailableEvents = bettingEntityFilter.filterCountries(allCountries, timeFilter);
        return BettingResponse.builder().entities(countriesWithAvailableEvents).build();
    }
    public Country getCountryById(int countryId) {
        return countryRepository.findById(countryId).orElseThrow(() -> new EntityNotFoundException(Country.class));
    }
}
