package com.betting.bets.stake_type;

import com.betting.events.sport.Sport;
import com.betting.events.sport.SportRepository;
import com.betting.test_builder.impl.SportBuilder;
import com.betting.test_builder.impl.StakeTypeBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StakeTypeRepositoryTest {
    @Autowired
    private StakeTypeRepository stakeTypeRepository;
    @Autowired
    private SportRepository sportRepository;
    private final Sport football = SportBuilder.aSport().withName("football").build();
    private final Sport hockey = SportBuilder.aSport().withName("hockey").build();
    private final Sport volleyball = SportBuilder.aSport().withName("volleyball").build();
    Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(football, 3),
                Arguments.of(hockey, 2),
                Arguments.of(volleyball, 1)
        );
    }
    @BeforeAll
    void beforeAll() {
        sportRepository.saveAll(List.of(football, hockey, volleyball));
        StakeTypeBuilder builder = StakeTypeBuilder.aStakeType();
        stakeTypeRepository.saveAll(List.of(
                builder.withSports(List.of(football, hockey, volleyball)).build(),
                builder.withSports(List.of(football)).buildOutcomeStakeType(),
                builder.withSports(List.of(hockey, football)).buildYesNoStakeType()
        ));
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void testFindStakeTypesBySportSuccess(Sport sport, int stakeTypesExpected) {
        List<StakeType> result = stakeTypeRepository.findStakeTypesBySport(sport.getId());
        assertEquals(stakeTypesExpected, result.size());
    }
    @AfterAll
    void afterAll() {
        stakeTypeRepository.deleteAll();
        sportRepository.deleteAll();
    }
}