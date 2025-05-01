package com.football.standing.controller;

import com.football.standing.dto.Teams;
import com.football.standing.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Teams", description = "Operations related to teams")
public class TeamController {
    @Autowired
    private StandingsService standingsService;

    @GetMapping("/team")
    @CrossOrigin(origins = "*")
    @Operation(
            summary = "Get all teams by league ID",
            description = "Returns a list of all teams in a league.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Teams[].class))),
                    @ApiResponse(responseCode = "404", description = "Teams not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class, example = "{\"error\": 404, \"message\": \"Countries not found!\"}"))),
            }
    )
    public Mono<ResponseEntity<List<Teams>>> getTeamByLeagueId(@RequestParam String leagueId) {
        return standingsService.getTeamByLeagueId(leagueId);
    }
}
