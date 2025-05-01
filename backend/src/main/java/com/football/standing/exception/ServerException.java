package com.football.standing.exception;

public class ServerException extends RuntimeException {
    private final Integer error;

    public ServerException(Integer error, String message) {
        super(message);
        this.error = error;
    }

    public Integer getError() {
        return error;
    }
}
