package br.com.compasso.desafio.domain.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebError {

    @JsonProperty("status_code")
    int statusCode;

    String message;

    public WebError() {
        // Empty deserialization constructor
    }

    public WebError(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
