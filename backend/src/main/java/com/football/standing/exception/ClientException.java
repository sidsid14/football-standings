package com.football.standing.exception;

public class ClientException extends RuntimeException {
    private final Integer error;

    public ClientException(Integer error, String message) {
        super(message);
        this.error = error;
    }

    public Integer getError() {
        return error;
    }


}
