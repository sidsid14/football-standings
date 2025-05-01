package com.football.standing.service.strategy;

import com.football.standing.dto.LeagueStanding;

import java.io.IOException;
import java.util.List;

public interface StandingsFetchingStrategy {
    List<LeagueStanding> fetchStandings(String leagueId) throws IOException;

}
