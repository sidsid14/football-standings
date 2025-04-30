package com.football.standing.client;

import com.football.standing.dto.LeagueStanding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RestFootballApiClient implements FootballAPIClient{

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.football.base-url}")
    private String baseUrl;

    @Value("${api.football.key}")
    private String apiKey;

    private final String ACTION_GET_STANDINGS = "get_standings";

    private static final Logger logger = LoggerFactory.getLogger(RestFootballApiClient.class);

    @Override
    public List<LeagueStanding> fetchStandings(String leagueId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("action", ACTION_GET_STANDINGS)
                .queryParam("league_id", leagueId)
                .queryParam("APIkey", apiKey);
        try {
            return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LeagueStanding>>() {}
            ).getBody();
        } catch (Exception e) {
            logger.error("Error fetching from API: " + e.getMessage());
            return null;

        }
    }


    public <T> T callApi(String action, Class<T> responseType, String... queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("action", action)
                .queryParam("APIkey", apiKey);

        for (int i = 0; i < queryParams.length; i += 2) {
            builder.queryParam(queryParams[i], queryParams[i + 1]);
        }

        try {
            return restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<T>() {}
            ).getBody();
        } catch (Exception e) {
            logger.error("Error fetching from API: " + e.getMessage());
            return null;
        }
    }


}
