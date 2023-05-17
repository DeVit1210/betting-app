package com.betting.bets.stake_type;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("prematch/stakeType")
@RequiredArgsConstructor
public class StakeTypeController {
    private final StakeTypeService stakeTypeService;

    @PostMapping("/add")
    public ResponseEntity<String> addStakeType(@RequestBody StakeTypeRequest request) {
        return ResponseEntity.ok(stakeTypeService.addStakeType(request));
    }

    @GetMapping("/get")
    public ResponseEntity<BettingResponse> getStakeTypesBySport(@RequestParam Integer sportId) {
        return ResponseEntity.ok(
                BettingResponse.builder().entities((stakeTypeService.findStakeTypesBySport(sportId))).build()
        );
    }
}
