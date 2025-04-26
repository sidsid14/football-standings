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

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiFetchingStrategyTest {

    @Mock
    private RestFootballApiClient restFootballApiClient;

    @InjectMocks
    private ApiFetchingStrategy apiFetchingStrategy;

    @Test
        void testFetchStandingsSuccess() throws IOException {
            String leagueId = "123";
            List<LeagueStanding> mockStandings = List.of(new LeagueStanding());

            when(restFootballApiClient.fetchStandings(leagueId)).thenReturn(mockStandings);

            List<LeagueStanding> result = apiFetchingStrategy.fetchStandings(leagueId);

            assertNotNull(result);
            assertEquals(mockStandings, result);
            verify(restFootballApiClient, times(1)).fetchStandings(leagueId);
        }

        @Test
        void testFetchStandingsReturnsNull() throws IOException {
            String leagueId = "123";

            when(restFootballApiClient.fetchStandings(leagueId)).thenReturn(null);

            List<LeagueStanding> result = apiFetchingStrategy.fetchStandings(leagueId);

            assertNull(result);
            verify(restFootballApiClient, times(1)).fetchStandings(leagueId);
        }

        @Test
            void testFetchCountriesSuccess() throws IOException {
                List<Countries> mockCountries = List.of(new Countries());

                when(restFootballApiClient.callApi("get_countries", List.class)).thenReturn(mockCountries);

                List<Countries> result = apiFetchingStrategy.fetchCountries();

                assertNotNull(result);
                assertEquals(mockCountries, result);
                verify(restFootballApiClient, times(1)).callApi("get_countries", List.class);
            }

            @Test
            void testFetchTeamByLeagueIdSuccess() throws IOException {
                String leagueId = "123";
                List<Teams> mockTeams = List.of(new Teams());

                when(restFootballApiClient.callApi("get_teams", List.class, "league_id", leagueId)).thenReturn(mockTeams);

                List<Teams> result = apiFetchingStrategy.fetchTeamByLeagueId(leagueId);

                assertNotNull(result);
                assertEquals(mockTeams, result);
                verify(restFootballApiClient, times(1)).callApi("get_teams", List.class, "league_id", leagueId);
            }

            @Test
            void testFetchLeagueByCountryIdSuccess() throws IOException {
                String countryId = "456";
                List<League> mockLeagues = List.of(new League());

                when(restFootballApiClient.callApi("get_leagues", List.class, "country_id", countryId)).thenReturn(mockLeagues);

                List<League> result = apiFetchingStrategy.fetchLeagueByCountryId(countryId);

                assertNotNull(result);
                assertEquals(mockLeagues, result);
                verify(restFootballApiClient, times(1)).callApi("get_leagues", List.class, "country_id", countryId);
            }


}