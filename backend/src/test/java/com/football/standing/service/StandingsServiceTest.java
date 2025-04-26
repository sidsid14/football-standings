package com.football.standing.service;

import com.football.standing.dto.Countries;
import com.football.standing.dto.League;
import com.football.standing.dto.LeagueStanding;
import com.football.standing.dto.Teams;
import com.football.standing.service.strategy.ApiFetchingStrategy;
import com.football.standing.service.strategy.CacheFetchingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingsServiceTest {
    @Mock
    private ApiFetchingStrategy apiFetchingStrategy;

    @Mock
    private CacheFetchingStrategy cacheFetchingStrategy;

    @InjectMocks
    private StandingsService standingsService;

    // Test case for offline mode fetching from cache
    @Test
    void testGetStandingsOfflineMode() throws IOException {
        String leagueId = "123";
        List<LeagueStanding> mockStandings = List.of(new LeagueStanding());

        when(cacheFetchingStrategy.fetchStandings(leagueId)).thenReturn(mockStandings);

        CollectionModel<LeagueStanding> result = standingsService.getStandings(leagueId, true);

        assertNotNull(result);
        assertIterableEquals(mockStandings, result.getContent());
        verify(cacheFetchingStrategy, times(1)).fetchStandings(leagueId);
        verifyNoInteractions(apiFetchingStrategy);
    }

    // Test case for online mode fetching from API and caching
@Test
void testGetStandingsOnlineModeWithCaching() throws IOException {
    String leagueId = "123";
    List<LeagueStanding> mockStandings = List.of(new LeagueStanding());

    when(apiFetchingStrategy.fetchStandings(leagueId)).thenReturn(mockStandings);

    CollectionModel<LeagueStanding> result = standingsService.getStandings(leagueId, false);

    assertNotNull(result);
    assertIterableEquals(mockStandings, result.getContent()); // Extract content for comparison
    verify(apiFetchingStrategy, times(1)).fetchStandings(leagueId);
    verify(cacheFetchingStrategy, times(1)).cacheStandings(leagueId, mockStandings);
}

    // Test case for online mode with null standings from API
    @Test
    void testGetStandingsOnlineModeWithNullStandings() throws IOException {
        String leagueId = "123";

        when(apiFetchingStrategy.fetchStandings(leagueId)).thenReturn(null);

        CollectionModel<LeagueStanding> result = standingsService.getStandings(leagueId, false);

        assertNull(result);
        verify(apiFetchingStrategy, times(1)).fetchStandings(leagueId);
        verify(cacheFetchingStrategy, never()).cacheStandings(anyString(), anyList());
    }

    // Test case for getCountries
        @Test
        void testGetCountries() throws IOException {
            List<Countries> mockCountries = List.of(new Countries());

            when(apiFetchingStrategy.fetchCountries()).thenReturn(mockCountries);

            List<Countries> result = standingsService.getCountries();

            assertNotNull(result);
            assertEquals(mockCountries, result);
            verify(apiFetchingStrategy, times(1)).fetchCountries();
        }

        // Test case for getTeamByLeagueId
        @Test
        void testGetTeamByLeagueId() throws IOException {
            String leagueId = "123";
            List<Teams> mockTeams = List.of(new Teams());

            when(apiFetchingStrategy.fetchTeamByLeagueId(leagueId)).thenReturn(mockTeams);

            List<Teams> result = standingsService.getTeamByLeagueId(leagueId);

            assertNotNull(result);
            assertEquals(mockTeams, result);
            verify(apiFetchingStrategy, times(1)).fetchTeamByLeagueId(leagueId);
        }

        // Test case for getLeagueByCountryId
        @Test
        void testGetLeagueByCountryId() throws IOException {
            String countryId = "456";
            List<League> mockLeagues = List.of(new League());

            when(apiFetchingStrategy.fetchLeagueByCountryId(countryId)).thenReturn(mockLeagues);

            List<League> result = standingsService.getLeagueByCountryId(countryId);

            assertNotNull(result);
            assertEquals(mockLeagues, result);
            verify(apiFetchingStrategy, times(1)).fetchLeagueByCountryId(countryId);
        }
}