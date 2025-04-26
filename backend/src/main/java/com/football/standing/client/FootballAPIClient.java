package com.football.standing.client;

import com.football.standing.dto.LeagueStanding;

import java.util.List;

public interface FootballAPIClient {
    List<LeagueStanding> fetchStandings(String leagueId);
}
