package com.betting.events.event;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prematch")
public class EventPrematchController {
    private final EventService eventService;
    @GetMapping("/getTopEventsList")
    public ResponseEntity<BettingResponse> getTopEvents() {
        return ResponseEntity.ok(eventService.getTopEvents());
    }
    @GetMapping("/getAllEvents")
    public ResponseEntity<BettingResponse> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllFutureEvents());
    }
    @GetMapping("/getEventList")
    public ResponseEntity<BettingResponse> getEvents(@RequestParam Long tournamentId,
                                                     @RequestParam Integer timeFilter) {
        // TODO: add time filter
        return ResponseEntity.ok(eventService.getEventsByTournament(tournamentId, timeFilter));
    }

    @GetMapping("/getEvent")
    public ResponseEntity<Event> getEvent(@RequestParam Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }
}
