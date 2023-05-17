package com.betting.events.event;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prematch/event")
public class EventPrematchController {
    private final EventService eventService;

    @GetMapping("/getTopList")
    public ResponseEntity<BettingResponse> getTopEvents() {
        return ResponseEntity.ok(eventService.getTopEvents());
    }

    @GetMapping("")
    public ResponseEntity<BettingResponse> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllFutureEvents());
    }

    @GetMapping("/getByTournament")
    public ResponseEntity<BettingResponse> getEvents(@RequestParam Long tournamentId,
                                                     @RequestParam Integer timeFilter) {
        // TODO: add time filter
        return ResponseEntity.ok(eventService.getEventsByTournament(tournamentId, timeFilter));
    }
    @GetMapping("/getEvent")
    public ResponseEntity<Event> getEvent(@RequestParam Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PostMapping("/add/{tournamentId}")
    public ResponseEntity<BettingEntity> addEvent(@RequestBody EventAddingRequest request,
                                                  @PathVariable Long tournamentId) {
        return ResponseEntity.ok(eventService.addEvent(request, tournamentId));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Event> deleteEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.deleteEvent(eventId));
    }
}
