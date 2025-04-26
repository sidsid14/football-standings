package com.football.standing.controller;

import com.football.standing.dto.League;
import com.football.standing.service.StandingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class LeagueController {
    @Autowired
    private StandingsService standingsService;

    @GetMapping("/league")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getLeagueByCountryId(@RequestParam String countryId) {

        List<League> league = standingsService.getLeagueByCountryId(countryId);
        if (league == null || league.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "League not found!"));
        }

        return ResponseEntity.ok(league);
    }
}
