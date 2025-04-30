package com.football.standing.service.strategy;

import com.football.standing.client.RestFootballApiClient;
import com.football.standing.dto.Countries;
import com.football.standing.dto.League;
import com.football.standing.dto.LeagueStanding;
import com.football.standing.dto.Teams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ApiFetchingStrategy implements StandingsFetchingStrategy {

    @Autowired
    private RestFootballApiClient restFootballApiClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiFetchingStrategy.class);
    @Override
    public List<LeagueStanding> fetchStandings(String leagueId)  {
        return restFootballApiClient.fetchStandings(leagueId);
    }

    public List<Countries> fetchCountries() {
        try {
            return (List<Countries>) restFootballApiClient.callApi("get_countries", List.class);
        }catch (Exception e){
            logger.error("Error fetching countries: " + e.getMessage());
            return null;
        }
    }

    public List<Teams> fetchTeamByLeagueId(String leagueId) {
        try {
            return (List<Teams>) restFootballApiClient.callApi("get_teams", List.class, "league_id", leagueId);
        }catch (Exception e){
            logger.error("Error fetching teams by league ID: " + e.getMessage());
            return null;
        }

    }

    public List<League> fetchLeagueByCountryId(String countryId) {
        try{
            return (List<League>) restFootballApiClient.callApi("get_leagues", List.class, "country_id", countryId);
        }catch (Exception e){
            logger.error("Error fetching leagues by country ID: " + e.getMessage());
            return null;
        }

    }
}
