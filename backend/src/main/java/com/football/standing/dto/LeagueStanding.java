package com.football.standing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
public class LeagueStanding {
    public String country_name;
    public String league_id;
    public String league_name;
    public String team_id;
    public String team_name;
    public String overall_league_position;
}
