package com.betting.events.sport;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/prematch")
public class SportPrematchController {
    private final SportService sportService;
    @GetMapping("/getSportsWithCount")
    public ResponseEntity<BettingResponse> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSportsWithCount());
    }
    @GetMapping("/getTopSportsList")
    public ResponseEntity<BettingResponse> getTopSports() {
        return ResponseEntity.ok(sportService.getTopSports());
    }
}

