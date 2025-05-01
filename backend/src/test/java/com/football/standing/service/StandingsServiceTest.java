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
import org.springframework.http.ResponseEntity;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

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

    @Test
    void testGetCountries() {
        Countries country1 = new Countries("44", "England", "https://apiv3.apifootball.com/badges/logo_country/44_england.png");
        Countries country2 = new Countries("3", "France", "https://apiv3.apifootball.com/badges/logo_country/3_france.png");

        List<Countries> mockCountries = List.of(country1, country2);
        when(apiFetchingStrategy.fetchCountries()).thenReturn(new Mono<ResponseEntity<List<Countries>>>() {
            @Override
            public void subscribe(CoreSubscriber<? super ResponseEntity<List<Countries>>> coreSubscriber) {
                ResponseEntity<List<Countries>> responseEntity = ResponseEntity.ok(mockCountries);
                coreSubscriber.onNext(responseEntity);
                coreSubscriber.onComplete();
            }
        });

        Mono<ResponseEntity<List<Countries>>> result = standingsService.getCountries();

        assertNotNull(result);
        assertEquals(mockCountries, result.block().getBody());
        verify(apiFetchingStrategy, times(1)).fetchCountries();
    }

    // Test case for getTeamByLeagueId
    @Test
    void testGetTeamByLeagueId() throws IOException {
        String leagueId = "123";
        Teams team1 = new Teams("1", "Team A", "https://apiv3.apifootball.com/badges/logo_team/1_team_a.png");
        Teams team2 = new Teams("2", "Team B", "https://apiv3.apifootball.com/badges/logo_team/2_team_b.png");
        List<Teams> mockTeams = List.of(team1, team2);

        when(apiFetchingStrategy.fetchTeamByLeagueId(leagueId)).thenReturn(new Mono<ResponseEntity<List<Teams>>>() {
            @Override
            public void subscribe(CoreSubscriber<? super ResponseEntity<List<Teams>>> coreSubscriber) {
                ResponseEntity<List<Teams>> responseEntity = ResponseEntity.ok(mockTeams);
                coreSubscriber.onNext(responseEntity);
                coreSubscriber.onComplete();
            }
        });


        Mono<ResponseEntity<List<Teams>>> result = standingsService.getTeamByLeagueId(leagueId);

        assertNotNull(result);
        assertEquals(mockTeams, result.block().getBody());
        verify(apiFetchingStrategy, times(1)).fetchTeamByLeagueId(leagueId);
    }

    @Test
    void testGetLeagueByCountryId() throws IOException {
        String countryId = "456";
        League league1 = new League("1", "Country 1", "https://apiv3.apifootball.com/badges/logo_league/1_league_a.png", "League A");
        League league2 = new League("2", "Country 2", "https://apiv3.apifootball.com/badges/logo_league/2_league_b.png", "League B");
        List<League> mockLeagues = List.of(league1, league2);
        when(apiFetchingStrategy.fetchLeagueByCountryId(countryId)).thenReturn(new Mono<ResponseEntity<List<League>>>() {
            @Override
            public void subscribe(CoreSubscriber<? super ResponseEntity<List<League>>> coreSubscriber) {
                ResponseEntity<List<League>> responseEntity = ResponseEntity.ok(mockLeagues);
                coreSubscriber.onNext(responseEntity);
                coreSubscriber.onComplete();
            }
        });

        Mono<ResponseEntity<List<League>>> result = standingsService.getLeagueByCountryId(countryId);

        assertNotNull(result);
        assertEquals(mockLeagues, result.block().getBody());
        verify(apiFetchingStrategy, times(1)).fetchLeagueByCountryId(countryId);
    }
}