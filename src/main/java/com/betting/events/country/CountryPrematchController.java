package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prematch/country")
public class CountryPrematchController {
    private final CountryService countryService;

    @GetMapping("")
    public ResponseEntity<BettingResponse> getCountryList(@RequestParam Integer sportId,
                                                          @RequestParam Integer timeFilter) {
        return ResponseEntity.ok(countryService.getCountriesWithAvailableEvents(sportId, timeFilter));
    }

    @PostMapping("/add/{sportId}")
    public ResponseEntity<CountryDto> addCountry(@RequestParam String countryName,
                                                 @PathVariable Integer sportId) {
        return ResponseEntity.ok(countryService.addCountry(countryName, sportId));
    }

    @DeleteMapping("/delete/{countryId}")
    public ResponseEntity<CountryDto> deleteCountry(@PathVariable Integer countryId) {
        return ResponseEntity.ok(countryService.deleteCountry(countryId));
    }
}
