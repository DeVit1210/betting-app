package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.exceptions.InvalidRequestParameterException;
import com.betting.events.sport.Sport;
import com.betting.events.sport.SportService;
import com.betting.events.util.BettingEntityFilter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class CountryServiceTest {
    @InjectMocks
    private CountryService countryService;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private SportService sportService;
    @Mock
    private BettingEntityFilter bettingEntityFilter;
    @Value("${test.exception.country-not-found}")
    private String messageException;
    // TODO: reformat timeFilters, add filtersHoursQuantity
    @Value("${test.emptyTimeFilter}")
    private Integer emptyTimeFilter;
    @Value("${test.timeFilter}")
    private Integer nonEmptyTimeFilter;
    @Value("${test.exception.invalid-time-filter}")
    private String invalidTimeFilterExceptionMessage;
    private final List<Country> countries = getMockList(Country.class, 2);
    @Test
    void testGetCountryByIdSuccess() {
        Country country = mock(Country.class);
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(country));
        Country foundCountry = countryService.getCountryById(1);
        verify(countryRepository, times(1)).findById(1);
        assertNotNull(foundCountry);
    }
    @Test
    void testGetCountryByIdNotFound() {
        when(countryRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() ->countryService.getCountryById(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(messageException);
    }
    @Test
    void testGetCountriesWithAvailableEventsSportNotFound() {
        when(sportService.getSport(anyInt())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class,
                () -> countryService.getCountriesWithAvailableEvents(1, emptyTimeFilter));
    }

    @Test
    void testGetCountriesWithAvailableEventsTimeFilterNotFound() {
        Sport sport = mock(Sport.class);
        when(sportService.getSport(anyInt())).thenReturn(sport);
        when(countryRepository.findAllCountriesBySport(any(Sport.class))).thenReturn(Streamable.of(countries));
        when(bettingEntityFilter.filterCountries(any(Streamable.class), anyInt()))
                .thenThrow(new InvalidRequestParameterException(invalidTimeFilterExceptionMessage));
        assertThatThrownBy(() -> countryService.getCountriesWithAvailableEvents(1, emptyTimeFilter))
                .isInstanceOf(InvalidRequestParameterException.class)
                .hasMessage(invalidTimeFilterExceptionMessage);
    }
    @Test
    void testGetCountriesWithAvailableEventsNoAvailableEventsEmptyTimeFilter() {
        setUpBeforeGetCountriesTest(false);
        BettingResponse response = countryService.getCountriesWithAvailableEvents(1, emptyTimeFilter);
        assertNotNull(response.getEntities());
    }
    @Test
    void testGetCountriesWithAvailableEventsSuccessEmptyTimeFilter() {
        setUpBeforeGetCountriesTest(true);
        BettingResponse response = countryService.getCountriesWithAvailableEvents(1, emptyTimeFilter);
        assertNotNull(response.getEntities());
    }
    @Test
    void testGetCountriesWithAvailableEventsSuccessWithTimeFilter() {
        setUpBeforeGetCountriesTest(true);
        BettingResponse response = countryService.getCountriesWithAvailableEvents(1, nonEmptyTimeFilter);
        assertNotNull(response.getEntities());
    }
    @Test
    void testGetCountriesWithAvailableEventsNoAvailableEventsWithTimeFilter() {
        setUpBeforeGetCountriesTest(false);
        BettingResponse response = countryService.getCountriesWithAvailableEvents(1, nonEmptyTimeFilter);
        assertNotNull(response.getEntities());
    }
    <T> List<T> getMockList(Class<T> classType, int listSize) {
        List<T> list = new ArrayList<>();
        IntStream.range(0,listSize).forEach(value -> list.add(mock(classType)));
        return list;
    }
    void setUpBeforeGetCountriesTest(boolean success) {
        Sport sport = mock(Sport.class);
        when(countryRepository.findAllCountriesBySport(any(Sport.class))).thenReturn(Streamable.of(countries));
        when(sportService.getSport(anyInt())).thenReturn(sport);
        when(bettingEntityFilter.filterCountries(Streamable.of(anyList()), anyInt()))
                .thenReturn(success ? countries : Collections.emptyList());
    }
}