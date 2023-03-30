package com.betting.events.country;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
