package com.football.standing.service;

import com.football.standing.dto.Countries;
import com.football.standing.dto.League;
import com.football.standing.dto.LeagueStanding;
import com.football.standing.dto.Teams;
import com.football.standing.service.strategy.ApiFetchingStrategy;
import com.football.standing.service.strategy.CacheFetchingStrategy;
import com.football.standing.util.HateoasLinkBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandingsService {
    @Autowired
    private ApiFetchingStrategy apiFetchingStrategy;
    @Autowired
    private CacheFetchingStrategy cacheFetchingStrategy;

    private static final Logger logger = LoggerFactory.getLogger(StandingsService.class);

    public CollectionModel<LeagueStanding> getStandings(String leagueId, boolean offlineMode) {
        // Logic to fetch standings from the database or cache
        List<LeagueStanding> standings = null;
        if (offlineMode || cacheFetchingStrategy.isCached("get_standings", leagueId)) {
            logger.info("Fetching standings from cache for leagueId: {}", leagueId);
            standings = cacheFetchingStrategy.fetchStandings(leagueId);
        } else {
            logger.info("Fetching standings from API for leagueId: {}", leagueId);
            standings = apiFetchingStrategy.fetchStandings(leagueId);
            if (standings != null) {
                cacheFetchingStrategy.cacheStandings(leagueId, standings);
            }
        }

        if (standings == null || standings.isEmpty()) {
            logger.error("Standings not found for leagueId: {}", leagueId);
            return null;
        } else {
            return HateoasLinkBuilder.createStandingsResources(standings, leagueId);
        }

    }

    public List<Countries> getCountries() {
        logger.info("Fetching countires from API");
        return apiFetchingStrategy.fetchCountries();
    }

    public List<Teams> getTeamByLeagueId(String leagueId) {
        logger.info("Fetching teams from API for leagueId: {}", leagueId);
        return apiFetchingStrategy.fetchTeamByLeagueId(leagueId);
    }

    public List<League> getLeagueByCountryId(String countryId) {
        logger.info("Fetching leagues from API for countryId: {}", countryId);
        return apiFetchingStrategy.fetchLeagueByCountryId(countryId);
    }

}
