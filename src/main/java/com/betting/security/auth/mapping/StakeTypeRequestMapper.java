package com.betting.security.auth.mapping;

import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.StakeTypeFactory;
import com.betting.bets.stake_type.StakeTypeRequest;
import com.betting.bets.stake_type.StakeTypeSpec;
import com.betting.events.sport.Sport;
import com.betting.events.sport.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StakeTypeRequestMapper implements ObjectMapper<StakeTypeRequest, StakeType> {
    private final SportService sportService;
    private final StakeTypeFactory stakeTypeFactory;
    @Override
    public StakeType mapFrom(StakeTypeRequest request) {
        List<Sport> sports = request.getAssociatedSportIndexes().stream()
                .map(sportService::getSport)
                .toList();
        StakeTypeSpec stakeTypeSpec = StakeTypeSpec.builder()
                .resultTypeName(request.getResultTypeName())
                .classTypeName(request.getClassTypeName())
                .sports(sports)
                .stakes(Collections.emptyList())
                .name(request.getStakeTypeName())
                .possibleBetNames(request.getPossibleBetNames())
                .build();
        return stakeTypeFactory.createStakeType(request.getStakeTypeVariant(), stakeTypeSpec);
    }
}
