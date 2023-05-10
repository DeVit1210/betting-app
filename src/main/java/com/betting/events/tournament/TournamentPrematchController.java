package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addTournament/{countryId}")
    public ResponseEntity<String> addTournament(@PathVariable Integer countryId,
                                                @RequestParam String tournamentName) {
        return ResponseEntity.ok(tournamentService.addTournament(countryId, tournamentName));
    }

    @DeleteMapping("/deleteTournament/{tournamentId}")
    public ResponseEntity<Tournament> deleteTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.deleteTournament(tournamentId));
    }

}
