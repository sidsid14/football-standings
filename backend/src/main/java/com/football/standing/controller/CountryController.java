package com.football.standing.controller;

import com.football.standing.dto.Countries;
import com.football.standing.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Country", description = "Operations related to countries")
public class CountryController {

    @Autowired
    private StandingsService standingsService;

    @GetMapping("/countries")
    @CrossOrigin(origins = "*")
    @Operation(
            summary = "Get all countries",
            description = "Returns a list of all countries",
            responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Countries[].class))),
            @ApiResponse(responseCode = "404", description = "Countries not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class, example = "{\"error\": 404, \"message\": \"Countries not found!\"}"))),
    }
    )
    public ResponseEntity<?> getCountries() {

        List<Countries> countries = standingsService.getCountries();
        if (countries == null || countries.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", 404, "message", "Countries not found!"));
        }

        return ResponseEntity.ok(countries);

    }
}
