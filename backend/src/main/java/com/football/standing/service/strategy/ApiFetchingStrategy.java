package com.football.standing.service.strategy;

import com.football.standing.client.RestFootballApiClient;
import com.football.standing.dto.Countries;
import com.football.standing.dto.League;
import com.football.standing.dto.LeagueStanding;
import com.football.standing.dto.Teams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ApiFetchingStrategy implements StandingsFetchingStrategy {

    @Autowired
    private RestFootballApiClient restFootballApiClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiFetchingStrategy.class);

    @Override
    public List<LeagueStanding> fetchStandings(String leagueId) {
        ResponseEntity<List<LeagueStanding>> leagueStandingsResponse = restFootballApiClient.fetchFromWebClient("get_standings", LeagueStanding.class, "league_id", leagueId)
                .block();

        return leagueStandingsResponse != null ? leagueStandingsResponse.getBody() : null;

    }

    public Mono<ResponseEntity<List<Countries>>> fetchCountries() {
        return restFootballApiClient.fetchFromWebClient("get_countries", Countries.class);
    }

    public Mono<ResponseEntity<List<Teams>>> fetchTeamByLeagueId(String leagueId) {
        return restFootballApiClient.fetchFromWebClient("get_teams", Teams.class, "league_id", leagueId);
    }

    public Mono<ResponseEntity<List<League>>> fetchLeagueByCountryId(String countryId) {
        return restFootballApiClient.fetchFromWebClient("get_leagues", League.class, "country_id", countryId);
    }


}
