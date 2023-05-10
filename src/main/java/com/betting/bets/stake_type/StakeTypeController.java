package com.betting.bets.stake_type;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stakeType")
@RequiredArgsConstructor
public class StakeTypeController {
    private final StakeTypeService stakeTypeService;
    @PostMapping("/add")
    public ResponseEntity<String> addStakeType(@RequestBody StakeTypeRequest request) {
        return ResponseEntity.ok(stakeTypeService.addStakeType(request));
    }
//    @GetMapping("/getStakeTypes")
//    public ResponseEntity<BettingResponse> getStakeTypesBySport(@RequestParam Integer eventId) {
//        return ResponseEntity.ok(stakeTypeService.findStakeTypesBySport(eventId));
//    }
}
