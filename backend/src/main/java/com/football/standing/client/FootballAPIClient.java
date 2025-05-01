package com.football.standing.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FootballAPIClient {
    <T> Mono<ResponseEntity<List<T>>> fetchFromWebClient(String action, Class<T> responseType, String... queryParams);

}
