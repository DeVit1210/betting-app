package com.betting.bets.stake;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stakes")
@RequiredArgsConstructor
public class StakeController {
    private final StakeService stakeService;
    @GetMapping("/generate")
    public ResponseEntity<BettingResponse> generateStakes(@RequestParam Long eventId) throws Exception {
        return ResponseEntity.ok(stakeService.generateStakes(eventId));
    }
    @PostMapping("/addFactors")
    public ResponseEntity<String> addStakeFactors(@RequestBody StakeAddingRequest request,
                                             @RequestParam Long eventId) {
        return ResponseEntity.ok(stakeService.addStakeFactors(request, eventId));
    }
}
