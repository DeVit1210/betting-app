package com.betting.bets.stake_type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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