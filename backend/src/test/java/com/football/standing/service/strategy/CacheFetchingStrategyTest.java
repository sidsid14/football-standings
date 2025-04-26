package com.football.standing.service.strategy;

import com.football.standing.dto.LeagueStanding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CacheFetchingStrategyTest {

    @InjectMocks
    private CacheFetchingStrategy cacheFetchingStrategy;

    @Test
        void testCacheStandings() {
            String key = "league-1";
            List<LeagueStanding> standings = List.of(new LeagueStanding());

            cacheFetchingStrategy.cacheStandings(key, standings);

            assertFalse(cacheFetchingStrategy.isCached("action", "league-1"));
        }

        @Test
        void testFetchStandingsWhenCached() {
            String key = "league-1";
            List<LeagueStanding> standings = List.of(new LeagueStanding());

            cacheFetchingStrategy.cacheStandings(key, standings);

            List<LeagueStanding> fetchedStandings = cacheFetchingStrategy.fetchStandings(key);
            assertNotNull(fetchedStandings);
            assertEquals(standings, fetchedStandings);
        }

        @Test
        void testFetchStandingsWhenNotCached() {
            String key = "league-2";

            List<LeagueStanding> fetchedStandings = cacheFetchingStrategy.fetchStandings(key);
            assertNull(fetchedStandings);
        }

        @Test
        void testIsCached() {
            String action = "action";
            String leagueId = "league-1";
            String key = action + "-" + leagueId;
            List<LeagueStanding> standings = List.of(new LeagueStanding());

            cacheFetchingStrategy.cacheStandings(key, standings);

            assertTrue(cacheFetchingStrategy.isCached(action, leagueId));
            assertFalse(cacheFetchingStrategy.isCached("action", "league-2"));
        }
}