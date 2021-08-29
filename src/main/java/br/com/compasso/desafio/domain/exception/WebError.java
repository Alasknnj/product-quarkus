package br.com.compasso.desafio.domain.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error object used to forward backend errors through a response body
 */
public class WebError {

    @JsonProperty("status_code")
    int statusCode;

    String message;

    public WebError(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
