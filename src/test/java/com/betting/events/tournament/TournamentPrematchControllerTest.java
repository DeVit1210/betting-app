package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.exceptions.EntityNotFoundException;
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

// TODO: add inheritance with BettingEntityPrematchControllerTest parent class

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
class TournamentPrematchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${test.id}")
    private Integer countryId;
    @Value("${test.timeFilter}")
    private Integer timeFilter;
    private final BettingResponse bettingResponse;
    private final List<Tournament> tournaments;
    private JSONArray jsonArray;

    public TournamentPrematchControllerTest() {
        tournaments = new ArrayList<>();
        tournaments.add(Tournament.builder().tournamentName("KHL").build());
        tournaments.add(Tournament.builder().tournamentName("NHL").build());
        tournaments.add(Tournament.builder().tournamentName("ExtraLeague").build());
        bettingResponse = new BettingResponse(tournaments);
    }

    @BeforeEach
    void setUp() throws Exception {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        jsonArray = (JSONArray) parser.parse(objectMapper.writeValueAsString(tournaments));
    }
    @Test
    void testGetTournamentByCountrySuccess() throws Exception {
        when(tournamentService.getTournamentsByCountryPrematch(anyInt(), anyInt())).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/tournament/getByCountry")
                        .param("countryId", String.valueOf(countryId))
                        .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").value(jsonArray));
    }

    @Test
    void testGetTournamentsByCountryEntityNotFound() throws Exception {
        when(tournamentService.getTournamentsByCountryPrematch(anyInt(), anyInt()))
                .thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/prematch/tournament/getByCountry")
                        .param("countryId", String.valueOf(countryId))
                        .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

}