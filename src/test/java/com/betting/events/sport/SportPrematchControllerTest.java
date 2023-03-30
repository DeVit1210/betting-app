package com.betting.events.sport;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.event.Event;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
class SportPrematchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SportService sportService;
    private final List<Event> entities;
    private final BettingResponse bettingResponse;
    private JSONArray array;
    public SportPrematchControllerTest() {
        entities = new ArrayList<>();
        entities.add(new Event("firstEvent", null, "first", "event", LocalDateTime.now(), false));
        entities.add(new Event("secondEvent", null, "second", "event", LocalDateTime.now(), false));
        entities.add(new Event("thirdEvent", null, "third", "event", LocalDateTime.now(), false));
        bettingResponse = new BettingResponse(entities);
    }

    @BeforeEach
    void setUp() throws Exception {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        array = (JSONArray) parser.parse(objectMapper.writeValueAsString(entities));
    }

    @Test
    void testGetSportsWithCount() throws Exception {
        when(sportService.getAllSportsWithCount()).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/getSportsWithCount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").isArray())
                .andExpect(jsonPath("$.entities").value(array));
    }

    @Test
    void testGetTopSportList() throws Exception {
        when(sportService.getTopSports()).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/getTopSportsList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").isArray())
                .andExpect(jsonPath("$.entities").value(array));
    }
}