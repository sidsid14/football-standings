package com.football.standing.controller;

import com.football.standing.dto.Teams;
import com.football.standing.service.StandingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TeamController {
    @Autowired
    private StandingsService standingsService;

    @GetMapping("/team")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getTeamByLeagueId(@RequestParam String leagueId) {
        List<Teams> teams = standingsService.getTeamByLeagueId(leagueId);
        if (teams == null || teams.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "Teams not found!"));
        }

        return ResponseEntity.ok(teams);

    }
}
