package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
// TODO: change class mapping (also for country controller)
@RequestMapping("/prematch/tournament")
public class TournamentPrematchController {
    private final TournamentService tournamentService;

    @GetMapping("/getByCountry")
    public ResponseEntity<BettingResponse> getTournamentsByCountry(@RequestParam Integer countryId,
                                                                   @RequestParam Integer timeFilter) {
        return ResponseEntity.ok(tournamentService.getTournamentsByCountryPrematch(countryId, timeFilter));
    }

    @PostMapping("/add/{countryId}")
    public ResponseEntity<TournamentDto> addTournament(@PathVariable Integer countryId,
                                                       @RequestParam String tournamentName) {
        return ResponseEntity.ok(tournamentService.addTournament(countryId, tournamentName));
    }

    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<TournamentDto> deleteTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.deleteTournament(tournamentId));
    }
}
