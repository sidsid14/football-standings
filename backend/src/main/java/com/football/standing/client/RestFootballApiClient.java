package com.football.standing.client;

import com.football.standing.exception.ClientException;
import com.football.standing.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class RestFootballApiClient implements FootballAPIClient {

    private static final Logger logger = LoggerFactory.getLogger(RestFootballApiClient.class);
    @Value("${api.football.base-url}")
    private String baseUrl;
    @Value("${api.football.key}")
    private String apiKey;
    @Autowired
    private WebClient webClient;

    public <T> Mono<ResponseEntity<List<T>>> fetchFromWebClient(String action, Class<T> responseType, String... queryParams) {
        UriComponentsBuilder uriBuilder = buildUri(action, queryParams);
        logger.info("Sending request to URL: {}", uriBuilder.toUriString());
        return webClient.get()
                .uri(uriBuilder.toUriString())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> handleClientError(response))
                .onStatus(status -> status.is5xxServerError(), response -> handleServerError(response))
                .bodyToMono(Object.class)
                .flatMap(response -> {
                    if (response instanceof Map) {
                        Map<String, Object> responseMap = (Map<String, Object>) response;
                        if (responseMap.containsKey("error")) {
                            Integer errorCode = (Integer) responseMap.get("error");
                            String errorMessage = (String) responseMap.get("message");
                            return Mono.error(new ClientException(errorCode, errorMessage));
                        } else {
                            return Mono.error(new ClientException(HttpStatus.BAD_REQUEST.value(), "Unexpected response format"));
                        }
                    } else if (response instanceof List) {
                        List<T> data = (List<T>) response;
                        return Mono.just(ResponseEntity.ok(data));
                    } else {
                        return Mono.error(new ClientException(HttpStatus.BAD_REQUEST.value(), "Unexpected response type"));
                    }
                })
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                .doOnError(error -> {
                    logger.error("Error while sending request: {}", error.getMessage());
                    throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending request - " + error.getMessage());
                });
    }

    private UriComponentsBuilder buildUri(String action, String... queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("action", action)
                .queryParam("APIkey", apiKey);

        for (int i = 0; i < queryParams.length; i += 2) {
            builder.queryParam(queryParams[i], queryParams[i + 1]);
        }

        return builder;
    }

    private Mono<Throwable> handleClientError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorMessage -> Mono.error(new ClientException(HttpStatus.BAD_REQUEST.value(), errorMessage)));
    }

    private Mono<Throwable> handleServerError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorMessage -> Mono.error(new ServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage)));
    }

}


