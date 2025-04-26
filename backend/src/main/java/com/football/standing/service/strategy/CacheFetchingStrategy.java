package com.football.standing.service.strategy;

import com.football.standing.dto.LeagueStanding;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheFetchingStrategy implements StandingsFetchingStrategy {

    private final Map<String, List<LeagueStanding>> cache = new ConcurrentHashMap<>();

    public void cacheStandings(String key, List<LeagueStanding> standings) {
        cache.put(key, standings);
    }
    @Override
    public List<LeagueStanding> fetchStandings(String leagueId) {
        return cache.getOrDefault(leagueId, null);
    }

    public boolean isCached(String action, String leagueId) {
        return cache.containsKey(action +"-"+leagueId);
    }
}
