package com.betting.results;

import com.betting.events.event.Event;
import com.betting.events.event.EventService;
import com.betting.events.sport.Sport;
import com.betting.security.auth.mapping.EventResultAddingRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventResultsService {
    private final EventResultsRepository eventResultsRepository;
    private final EventService eventService;
    private final ApplicationContext applicationContext;
    public ResponseEntity<?> addResults(Long eventId, EventResultAddingRequest request) {
        Event event = eventService.getEventById(eventId);
        Sport eventSport = event.getTournament().getCountry().getSport();
        EventResultAddingRequestMapper mapper = applicationContext.getBean(EventResultAddingRequestMapper.class,
                eventSport.getCombinatorType(),
                eventSport.getSportType()
        );
        EventResults eventResults = mapper.mapFrom(request);
        eventService.bind(event, eventResults);
        eventResultsRepository.save(eventResults);
        eventService.updateResults(event.getId(), eventResults);
        return ResponseEntity.ok().build();
    }
}
