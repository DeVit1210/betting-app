package com.betting.bets.stake_type;

import com.betting.events.sport.SportService;
import com.betting.mapping.StakeTypeRequestMapper;
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

    public List<StakeType> findStakeTypesBySport(Integer sportId) {
        return stakeTypeRepository.findStakeTypesBySport(sportId);
    }
}
