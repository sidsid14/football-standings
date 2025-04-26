package com.football.standing.util;

import com.football.standing.controller.CountryController;
import com.football.standing.controller.StandingsController;
import com.football.standing.controller.TeamController;
import com.football.standing.dto.LeagueStanding;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

public class HateoasLinkBuilder {

    public static CollectionModel<LeagueStanding> createStandingsResources(List<LeagueStanding> standings, String leagueId) {
        CollectionModel<LeagueStanding> collectionModel = CollectionModel.of(standings);
        collectionModel.add(linkTo(methodOn(StandingsController.class)
                .getStandings(leagueId, false))
                .withSelfRel());
        collectionModel.add(linkTo(methodOn(CountryController.class)
                .getCountries())
                .withRel("countries"));
        collectionModel.add(linkTo(methodOn(TeamController.class)
                .getTeamByLeagueId(leagueId))
                .withRel("teams"));
        return collectionModel;
    }
}