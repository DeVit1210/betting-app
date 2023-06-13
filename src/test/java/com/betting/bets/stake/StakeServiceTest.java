package com.betting.bets.stake;

import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.StakeTypeService;
import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.event.Event;
import com.betting.events.event.EventService;
import com.betting.events.sport.Sport;
import com.betting.events.tournament.Tournament;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.mapping.StakeDtoMapper;
import com.betting.test_builder.impl.StakeBuilder;
import com.betting.test_builder.impl.StakeTypeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class StakeServiceTest {
    @InjectMocks
    private StakeService stakeService;
    @Mock
    private StakeTypeService stakeTypeService;
    @Mock
    private StakeRepository stakeRepository;
    @Mock
    private EventService eventService;
    @Mock
    private BeanFactory beanFactory;
    @Value("${test.exception.stake-not-found}")
    private String stakeNotfoundMessage;

    static Stream<Arguments> successArgumentsProvider() {
        StakeTypeBuilder stakeTypeBuilder = StakeTypeBuilder.aStakeType();
        return Stream.of(
                Arguments.of(List.of(
                        stakeTypeBuilder.buildYesNoStakeType(),
                        stakeTypeBuilder.buildOutcomeStakeType(),
                        stakeTypeBuilder.withPossibleBetNames(List.of("ТБ 1.5", "ТМ 2.5", "ТМ 1.5", "ТБ 2.5")).build()
                ), 9),
                Arguments.of(List.of(
                        stakeTypeBuilder.withPossibleBetNames(List.of("ТБ 1.5", "ТМ 2.5", "ТМ 1.5", "ТБ 2.5")).build(),
                        stakeTypeBuilder.withPossibleBetNames(List.of("Ф1 +1.5", "Ф2 -2.5", "Ф1 -1.5", "Ф2 +2.5")).build()
                ), 8)
        );
    }

    static Stream<Arguments> addStakeArgumentsProvider() {
        StakeBuilder stakeBuilder = StakeBuilder.aStakeBuilder();
        Stake firstStake = stakeBuilder.withFullStakeName("fullStakeName1").build();
        Stake secondStake = stakeBuilder.withFullStakeName("fullStakeName2").build();
        Stake thirdStake = stakeBuilder.withFullStakeName("fullStakeName3").build();
        return Stream.of(
                Arguments.of(
                        List.of(firstStake, secondStake, thirdStake),
                        Map.of("fullStakeName1", 1.7f, "fullStakeName2", 2.12f),
                        2),
                Arguments.of(
                        List.of(firstStake, secondStake, thirdStake),
                        Map.of("fullStakeName2", 2.0f),
                        1)
                );
    }

    @ParameterizedTest
    @MethodSource("successArgumentsProvider")
    void testGenerateStakesSuccess(List<StakeType> stakeTypes, int expectedGeneratedStakesQuantity) throws Exception {
        Event event = mock(Event.class);
        when(eventService.getEventById(anyLong())).thenReturn(event);
        Sport sport = mock(Sport.class);
        Tournament tournament = mock(Tournament.class);
        Country country = mock(Country.class);
        when(event.getTournament()).thenReturn(tournament);
        when(tournament.getCountry()).thenReturn(country);
        when(country.getSport()).thenReturn(sport);
        when(stakeTypeService.findStakeTypesBySport(sport.getId())).thenReturn(stakeTypes);
        when(stakeRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(beanFactory.getBean(StakeDtoMapper.class)).thenReturn(new StakeDtoMapper());
        BettingResponse response = stakeService.generateStakes(1L);
        assertNotNull(response);
        System.out.println(response.getEntities());
        assertEquals(expectedGeneratedStakesQuantity, response.getEntities().size());
    }

    @Test
    void testGenerateStakesAlreadyExist() {
        Event event = mock(Event.class);
        when(event.getStakes()).thenReturn(List.of(StakeBuilder.aStakeBuilder().build()));
        when(eventService.getEventById(anyLong())).thenReturn(event);
        assertThatThrownBy(() -> stakeService.generateStakes(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stakes are already added!");
    }

    @ParameterizedTest
    @MethodSource("addStakeArgumentsProvider")
    void testAddStakeFactorsSuccess(List<Stake> stakes, Map<String, Float> stakeNamesAndFactors, int expectedFactorizedQuantity) {
        Event event = mock(Event.class);
        when(eventService.getEventById(anyLong())).thenReturn(event);
        when(event.getStakes()).thenReturn(stakes);
        StakeAddingRequest request = mock(StakeAddingRequest.class);
        when(request.getStakeNamesAndFactors()).thenReturn(stakeNamesAndFactors);
        when(stakeRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        doNothing().when(stakeRepository).deleteAll();
        String result = stakeService.addStakeFactors(request, 1L);
        assertEquals(expectedFactorizedQuantity, Integer.parseInt(result.split(" ")[0]));
    }

    @Test
    void testFindByIdSuccess() {
        when(stakeRepository.findById(anyLong())).thenReturn(Optional.of(StakeBuilder.aStakeBuilder().build()));
        assertDoesNotThrow(() -> stakeService.findById(1L));
    }

    @Test
    void testFindByIdNotFound() {
        when(stakeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stakeService.findById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(stakeNotfoundMessage);
    }
}