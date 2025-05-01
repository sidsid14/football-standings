package com.football.standing.service.strategy;

import com.football.standing.client.RestFootballApiClient;
import com.football.standing.dto.Countries;
import com.football.standing.dto.League;
import com.football.standing.dto.LeagueStanding;
import com.football.standing.dto.Teams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiFetchingStrategyTest {

    @Mock
    private RestFootballApiClient restFootballApiClient;

    @InjectMocks
    private ApiFetchingStrategy apiFetchingStrategy;

    @Test
    void testFetchStandingsSuccess() {
        String leagueId = "789";
        List<LeagueStanding> mockStandings = List.of(new LeagueStanding());
        ResponseEntity<List<LeagueStanding>> mockResponse = ResponseEntity.ok(mockStandings);

        when(restFootballApiClient.fetchFromWebClient("get_standings", LeagueStanding.class, "league_id", leagueId))
                .thenReturn(Mono.just(mockResponse));

        List<LeagueStanding> result = apiFetchingStrategy.fetchStandings(leagueId);

        assertNotNull(result);
        assertEquals(mockStandings, result);
        verify(restFootballApiClient, times(1)).fetchFromWebClient("get_standings", LeagueStanding.class, "league_id", leagueId);
    }

    @Test
    void testFetchCountriesSuccess() {
        List<Countries> mockCountries = List.of(new Countries("1", "CountryName", "CountryLogo"));
        ResponseEntity<List<Countries>> mockResponse = ResponseEntity.ok(mockCountries);

        when(restFootballApiClient.fetchFromWebClient("get_countries", Countries.class))
                .thenReturn(Mono.just(mockResponse));

        Mono<ResponseEntity<List<Countries>>> result = apiFetchingStrategy.fetchCountries();

        assertNotNull(result);
        ResponseEntity<List<Countries>> responseEntity = result.block();
        assertNotNull(responseEntity);
        assertEquals(mockCountries, responseEntity.getBody());
        verify(restFootballApiClient, times(1)).fetchFromWebClient("get_countries", Countries.class);
    }

    @Test
    void testFetchTeamByLeagueIdSuccess() {
        String leagueId = "123";
        List<Teams> mockTeams = List.of(new Teams("1", "TeamName", "TeamLogo"));
        ResponseEntity<List<Teams>> mockResponse = ResponseEntity.ok(mockTeams);

        when(restFootballApiClient.fetchFromWebClient("get_teams", Teams.class, "league_id", leagueId))
                .thenReturn(Mono.just(mockResponse));

        Mono<ResponseEntity<List<Teams>>> result = apiFetchingStrategy.fetchTeamByLeagueId(leagueId);

        assertNotNull(result);
        ResponseEntity<List<Teams>> responseEntity = result.block();
        assertNotNull(responseEntity);
        assertEquals(mockTeams, responseEntity.getBody());
        verify(restFootballApiClient, times(1)).fetchFromWebClient("get_teams", Teams.class, "league_id", leagueId);
    }

    @Test
    void testFetchLeagueByCountryIdSuccess() {
        String countryId = "456";
        List<League> mockLeagues = List.of(new League("1", "Country 1", "1", "League1"));
        ResponseEntity<List<League>> mockResponse = ResponseEntity.ok(mockLeagues);

        when(restFootballApiClient.fetchFromWebClient("get_leagues", League.class, "country_id", countryId))
                .thenReturn(Mono.just(mockResponse));

        Mono<ResponseEntity<List<League>>> result = apiFetchingStrategy.fetchLeagueByCountryId(countryId);

        assertNotNull(result);
        ResponseEntity<List<League>> responseEntity = result.block();
        assertNotNull(responseEntity);
        assertEquals(mockLeagues, responseEntity.getBody());
        verify(restFootballApiClient, times(1)).fetchFromWebClient("get_leagues", League.class, "country_id", countryId);
    }


}