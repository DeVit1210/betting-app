package com.betting.results;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class EventResultsController {
    private final EventResultsService eventResultsService;
    @PostMapping("/create")
    public ResponseEntity<?> addEventResults(@RequestParam Long eventId,
                                          @RequestBody EventResultAddingRequest request) {
        return ResponseEntity.ok(eventResultsService.addResults(eventId, request));
    }
}
