package com.betting.events.event;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.exceptions.InvalidRequestParameterException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc(addFilters = false)
class EventPrematchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${test.id}")
    private Integer tournamentId;
    @Value("${test.timeFilter}")
    private Integer timeFilter;
    @Value("${test.id}")
    private Long eventId;
    private final BettingResponse bettingResponse;
    private final List<Event> events;
    private JSONArray jsonArray;
    public EventPrematchControllerTest() {
        events = new ArrayList<>();
        events.add(new Event("firstEvent", null, "first", "event", LocalDateTime.now(), true));
        events.add(new Event("secondEvent", null, "second", "event", LocalDateTime.now(), true));
        events.add(new Event("thirdEvent", null, "third", "event", LocalDateTime.now(), true));
        bettingResponse = new BettingResponse(events);
    }

    @BeforeEach
    void setUp() throws Exception {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        jsonArray = (JSONArray) parser.parse(objectMapper.writeValueAsString(events));
    }

    @Test
    void testGetTopEvents() throws Exception {
        when(eventService.getTopEvents()).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/getTopEventsList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").value(jsonArray));
    }

    @Test
    void testGetAllEvents() throws Exception {
        when(eventService.getAllFutureEvents()).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/getAllEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").value(jsonArray));
    }

    @Test
    void testGetEvents() throws Exception {
        when(eventService.getEventsByTournament(anyLong(), anyInt())).thenReturn(bettingResponse);
        mockMvc.perform(get("/prematch/getEventList")
                    .param("tournamentId", String.valueOf(tournamentId))
                    .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").value(jsonArray));
    }

    @Test
    void testGetEventsTournamentNotFound() throws Exception {
        when(eventService.getEventsByTournament(anyLong(), anyInt())).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/prematch/getEventList")
                        .param("tournamentId", String.valueOf(tournamentId))
                        .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    void testGetEventsInvalidTimeFilter() throws Exception {
        when(eventService.getEventsByTournament(anyLong(), anyInt())).thenThrow(InvalidRequestParameterException.class);
        mockMvc.perform(get("/prematch/getEventList")
                        .param("tournamentId", String.valueOf(tournamentId))
                        .param("timeFilter", String.valueOf(timeFilter)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestParameterException));
    }

    @Test
    void testGetEvent() throws Exception {
        final String eventName = "event";
        Event event = new Event(eventName, null, "firstOpp",
                "secondOpp", LocalDateTime.now(), true);
        when(eventService.getEventById(anyLong())).thenReturn(event);
        mockMvc.perform(get("/prematch/getEvent")
                        .param("eventId", String.valueOf(eventId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(eventName));
    }

    @Test
    void testGetEventNotFound() throws Exception {
        when(eventService.getEventById(anyLong())).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/prematch/getEvent")
                        .param("eventId", String.valueOf(eventId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }
}