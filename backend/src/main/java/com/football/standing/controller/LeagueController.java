package com.football.standing.controller;

import com.football.standing.dto.League;
import com.football.standing.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Leagues", description = "Operations related to leagues")
public class LeagueController {
    @Autowired
    private StandingsService standingsService;

    @GetMapping("/league")
    @CrossOrigin(origins = "*")
    @Operation(
            summary = "Get all leagues by country ID",
            description = "Returns a list of all leagues by country ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = League[].class))),
                    @ApiResponse(responseCode = "404", description = "League not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class, example = "{\"error\": 404, \"message\": \"Countries not found!\"}"))),
            }
    )
    public ResponseEntity<?> getLeagueByCountryId(@RequestParam String countryId) {

        List<League> league = standingsService.getLeagueByCountryId(countryId);
        if (league == null || league.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "League not found!"));
        }

        return ResponseEntity.ok(league);
    }
}
