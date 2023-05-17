package com.betting.events.sport;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.mapping.SportDtoMapper;
import com.betting.mapping.SportRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {
    private final SportRepository sportRepository;
    private final BeanFactory beanFactory;
    public BettingResponse getAllSportsWithCount() {
        List<Sport> sports = sportRepository.findAll();
        sports.forEach(sport -> sport.setEventsQuantity(getSportEventsQuantity(sport)));
        List<Sport> sportsWithAvailableEvents = sports.stream().filter(sport -> sport.getEventsQuantity() > 0).toList();
        SportDtoMapper mapper = beanFactory.getBean(SportDtoMapper.class);
        List<SportDto> sportDtoList = sportsWithAvailableEvents.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(sportDtoList).build();
    }
    public BettingResponse getTopSports() {
        SportDtoMapper mapper = beanFactory.getBean(SportDtoMapper.class);
        return BettingResponse.builder()
                .entities(sportRepository.findAllSportByTopIsTrueOrderByNameAsc().map(mapper::mapFrom).toList())
                .build();
    }

    public Sport getSport(int id) {
        return sportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Sport.class));
    }

    public Integer getSportEventsQuantity(Sport sport) {
        return sport.getCountries().stream()
                .reduce(0, (midSum, country) ->
                                midSum + country.getTournaments().stream()
                                        .reduce(0, (mid, tournament) -> mid + tournament.getEvents().size(), Integer::sum)
                        , Integer::sum);
    }

    public SportDto addSport(SportRequest request) {
        SportRequestMapper mapper = beanFactory.getBean(SportRequestMapper.class);
        Sport sport = mapper.mapFrom(request);
        sportRepository.save(sport);
        SportDtoMapper sportDtoMapper = beanFactory.getBean(SportDtoMapper.class);
        return sportDtoMapper.mapFrom(sport);
    }
}
