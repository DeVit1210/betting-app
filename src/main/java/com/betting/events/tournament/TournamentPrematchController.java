package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
// TODO: change class mapping (also for country controller)
@RequestMapping("/prematch")
public class TournamentPrematchController {
    private final TournamentService tournamentService;
    @GetMapping("/getTournamentsList")
    public ResponseEntity<BettingResponse> getTournamentsByCountry(@RequestParam Integer countryId,
                                                                   @RequestParam Integer timeFilter) {
        return ResponseEntity.ok(tournamentService.getTournamentsByCountryPrematch(countryId, timeFilter));
    }

}
