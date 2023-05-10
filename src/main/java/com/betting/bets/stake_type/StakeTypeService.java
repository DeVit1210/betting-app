package com.betting.bets.stake_type;

import com.betting.events.sport.Sport;
import com.betting.events.sport.SportService;
import com.betting.security.auth.mapping.StakeTypeRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StakeTypeService {
    private final StakeTypeRepository stakeTypeRepository;
    private final SportService sportService;
    private final BeanFactory beanFactory;
    private final StakeTypeFactory stakeTypeFactory;
    public String addStakeType(StakeTypeRequest request) {
        StakeTypeRequestMapper mapper = beanFactory.getBean(StakeTypeRequestMapper.class, sportService, stakeTypeFactory);
        StakeType stakeType = mapper.mapFrom(request);
        stakeTypeRepository.save(stakeType);
        return "stake type successfully added!";
    }

    public List<StakeType> findStakeTypesBySport(Sport sport) {
         return stakeTypeRepository.findStakeTypesBySport(sport.getId());
//        List<StakeTypeDto> stakeTypeDtoList = stakeTypes.stream()
//                .map(stakeType -> StakeTypeDto.builder()
//                        .id(stakeType.getId())
//                        .name(stakeType.getName())
//                        .possibleNames(stakeType.getPossibleBetNames())
//                        .build())
//                .toList();
    }
}
