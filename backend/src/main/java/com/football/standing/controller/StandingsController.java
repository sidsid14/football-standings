package com.football.standing.controller;

import com.football.standing.dto.LeagueStanding;
import com.football.standing.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Standings", description = "Operations related to standings")
public class StandingsController {

    @Autowired
    private StandingsService standingsService;

    @GetMapping("/standings")
    @CrossOrigin(origins = "*")
    @Operation(
            summary = "Get a list of football standings by league ID",
            description = "Returns list of football standings by league ID. If offlineMode is true, it will return the standings from the local cache.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CollectionModel.class, subTypes = LeagueStanding.class))),
                    @ApiResponse(responseCode = "404", description = "League not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class, example = "{\"error\": 404, \"message\": \"Countries not found!\"}"))),
            }
    )
    public ResponseEntity<?> getStandings(@RequestParam String leagueId, @RequestParam(required = false, defaultValue = "false") Boolean offlineMode) {

        CollectionModel<LeagueStanding> standings = standingsService.getStandings(leagueId, offlineMode);
        if (standings == null) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "Standing not found!"));
        }

        return ResponseEntity.ok(standings);

    }

}
