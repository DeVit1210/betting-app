package com.betting.bets.stake;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.test_builder.impl.StakeDtoBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc(addFilters = false)
class StakeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StakeService stakeService;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Value("${test.id}")
    private Long eventId;
    private BettingResponse bettingResponse;
    public StakeControllerTest() {
        StakeDtoBuilder builder = StakeDtoBuilder.aStakeDtoBuilder();
        bettingResponse = BettingResponse.builder().entities(Collections.nCopies(3, builder.build())).build();
    }
    @Test
    void testGenerateStakesEndpoint() throws Exception {
        when(stakeService.generateStakes(eventId)).thenReturn(bettingResponse);
        mockMvc.perform(get("/stakes/generate")
                        .param("eventId", String.valueOf(eventId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entities").isArray());
    }

    @Test
    void testAddStakeFactors() throws Exception {
        final String successMessage = "10 stakes has been successfully added!";
        StakeAddingRequest request = new StakeAddingRequest();
        when(stakeService.addStakeFactors(any(StakeAddingRequest.class), anyLong())).thenReturn(successMessage);
        mockMvc.perform(post("/stakes/addFactors")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .param("eventId", String.valueOf(eventId)))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), successMessage));
    }
}