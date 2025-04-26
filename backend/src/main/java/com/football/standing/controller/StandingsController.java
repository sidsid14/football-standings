package com.football.standing.controller;

import com.football.standing.dto.Countries;
import com.football.standing.dto.League;
import com.football.standing.dto.LeagueStanding;
import com.football.standing.dto.Teams;
import com.football.standing.service.StandingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class StandingsController {

    @Autowired
    private StandingsService standingsService;

    @GetMapping("/standings")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getStandings(@RequestParam String leagueId, @RequestParam(required = false, defaultValue = "false") Boolean offlineMode) {

        CollectionModel<LeagueStanding> standings = standingsService.getStandings(leagueId, offlineMode);
        if (standings == null) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "Standing not found!"));
        }

        return ResponseEntity.ok(standings);

    }

}
