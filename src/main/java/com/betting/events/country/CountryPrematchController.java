package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prematch")
public class CountryPrematchController {
    private final CountryService countryService;
    @GetMapping("/getCountryList")
    public ResponseEntity<BettingResponse> getCountryList(@RequestParam Integer sportId,
                                                          @RequestParam Integer timeFilter) {
        return ResponseEntity.ok(countryService.getCountriesWithAvailableEvents(sportId, timeFilter));
    }
    @PostMapping("/addCountry/{sportId}")
    public ResponseEntity<String> addCountry(@RequestParam String countryName,
                                             @PathVariable Integer sportId) {
        return ResponseEntity.ok(countryService.addCountry(countryName, sportId));
    }
    @DeleteMapping("/deleteCountry/{countryId}")
    public ResponseEntity<Country> deleteCountry(@PathVariable Integer countryId) {
        return ResponseEntity.ok(countryService.deleteCountry(countryId));
    }
}
