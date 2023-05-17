package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.sport.Sport;
import com.betting.events.sport.SportService;
import com.betting.events.util.BettingEntityFilter;
import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.mapping.CountryDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final SportService sportService;
    private final BettingEntityFilter bettingEntityFilter;
    private final CountryDtoMapper mapper;
    public BettingResponse getCountriesWithAvailableEvents(int sportId, int timeFilter) {
        Sport sport = sportService.getSport(sportId);
        Streamable<Country> allCountries = countryRepository.findAllCountriesBySport(sport);
        List<Country> countriesWithAvailableEvents = bettingEntityFilter.filterCountries(allCountries, timeFilter);
        List<CountryDto> countryDtoList = countriesWithAvailableEvents.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(countryDtoList).build();
    }
    public Country getCountryById(int countryId) {
        return countryRepository.findById(countryId).orElseThrow(() -> new EntityNotFoundException(Country.class));
    }

    public CountryDto addCountry(String countryName, Integer sportId) {
        Sport sport = sportService.getSport(sportId);
        Country country = Country.builder()
                .tournaments(Collections.emptyList())
                .name(countryName)
                .sport(sport)
                .build();
        countryRepository.save(country);
        sport.getCountries().add(country);
        return mapper.mapFrom(country);
    }

    public CountryDto deleteCountry(Integer countryId) {
        Country country = getCountryById(countryId);
        Sport sport = country.getSport();
        // TODO: throw some custom exception
        ThrowableUtils.trueOrElseThrow(c -> !c.getTournaments().isEmpty(), country, new UnsupportedOperationException());
        sport.getCountries().remove(country);
        countryRepository.delete(country);
        return mapper.mapFrom(country);
    }
}
