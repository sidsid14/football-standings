package com.football.standing.controller;

import com.football.standing.dto.Countries;
import com.football.standing.service.StandingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CountryController {

    @Autowired
    private StandingsService standingsService;

    @GetMapping("/countries")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getCountries() {

        List<Countries> countries = standingsService.getCountries();
        if (countries == null || countries.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "Countries not found!"));
        }

        return ResponseEntity.ok(countries);

    }
}
