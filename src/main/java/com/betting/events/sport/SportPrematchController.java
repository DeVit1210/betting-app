package com.betting.events.sport;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prematch/sport")
public class SportPrematchController {
    private final SportService sportService;

    @GetMapping("/getAllWithCount")
    public ResponseEntity<BettingResponse> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSportsWithCount());
    }

    @GetMapping("/getTopList")
    public ResponseEntity<BettingResponse> getTopSports() {
        return ResponseEntity.ok(sportService.getTopSports());
    }

    @PostMapping("/add")
    public ResponseEntity<SportDto> addSport(@RequestBody SportRequest request) {
        return ResponseEntity.ok(sportService.addSport(request));
    }
}

