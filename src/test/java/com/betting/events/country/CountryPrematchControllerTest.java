package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.exception.EntityNotFoundException;
import com.betting.events.exception.InvalidRequestParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
class CountryPrematchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CountryService countryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${test.id}")
    private Integer sportId;
    @Value("${test.timeFilter}")
    private Integer timeFilter;
    private final BettingResponse bettingResponse;
    private final List<Country> countries;
    private JSONArray jsonArray;

    public CountryPrematchControllerTest() {
        countries = new ArrayList<>();
        countries.add(Country.builder().name("Belarus").build());
        countries.add(Country.builder().name("Russia").build());
        countries.add(Country.builder().name("Ukraine").build());
        bettingResponse = new BettingResponse(countries);
    }

    @BeforeEach
    void setUp() throws Exception {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        jsonArray = (JSONArray) parser.parse(objectMapper.writeValueAsString(countries));
    }

    @Test
    void testGetCountryListSuccess() throws Exception {
        when(countryService.getCountriesWithAvailableEvents(anyInt(), anyInt())).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/getCountryList")
                    .param("sportId", String.valueOf(sportId))
                    .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").value(jsonArray));
    }

    @Test
    void testGetCountryListInvalidTimeFilter() throws Exception {
        when(countryService.getCountriesWithAvailableEvents(anyInt(), anyInt()))
                .thenThrow(InvalidRequestParameterException.class);
        mockMvc.perform(get("/prematch/getCountryList")
                        .param("sportId", String.valueOf(sportId))
                        .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestParameterException));
    }

    @Test
    void testGetCountryListSportNotFound() throws Exception {
        when(countryService.getCountriesWithAvailableEvents(anyInt(), anyInt()))
                .thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/prematch/getCountryList")
                        .param("sportId", String.valueOf(sportId))
                        .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }
}