package com.betting.bets.stake;

import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.StakeTypeService;
import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.event.Event;
import com.betting.events.event.EventService;
import com.betting.events.sport.Sport;
import com.betting.security.auth.mapping.StakeDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StakeService {
    private final StakeTypeService stakeTypeService;
    private final StakeRepository stakeRepository;
    private final EventService eventService;
    private final BeanFactory beanFactory;
    public BettingResponse generateStakes(Long eventId) throws Exception {
        Event event = eventService.getEventById(eventId);
        Sport sport = event.getTournament().getCountry().getSport();
        List<StakeType> stakeTypeList = stakeTypeService.findStakeTypesBySport(sport);
        List<Stake> stakes = new ArrayList<>();
        for(StakeType stakeType : stakeTypeList) {
            stakes.addAll(stakeType.generateStakes(event));
        }
        stakeRepository.saveAll(stakes);
        StakeDtoMapper mapper = beanFactory.getBean(StakeDtoMapper.class);
        List<StakeDto> stakeDtos = stakes.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(stakeDtos).build();
    }

    public String addStakeFactors(StakeAddingRequest request, Long eventId) {
        Event event = eventService.getEventById(eventId);
        List<Stake> stakes = event.getStakes();
        Map<String, Float> stakeNamesAndFactors = request.getStakeNamesAndFactors();
        List<Stake> factorizedStakes = stakes.stream()
                .filter(stake -> stakeNamesAndFactors.containsKey(stake.getFullStakeName()))
                .toList();
        factorizedStakes.forEach(stake -> stake.setFactor(stakeNamesAndFactors.get(stake.getFullStakeName())));
        List<Stake> nonFactorizedStakes = stakes.stream().filter(stake -> stake.getFactor() == 0.0f).toList();
        stakeRepository.saveAll(factorizedStakes);
        stakeRepository.deleteAll(nonFactorizedStakes);
        return factorizedStakes.size() + " stakes has been successfully added!";
    }
}