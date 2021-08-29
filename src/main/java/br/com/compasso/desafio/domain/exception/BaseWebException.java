package br.com.compasso.desafio.domain.exception;

public abstract class BaseWebException extends RuntimeException {
    private final int statusCode;

    public BaseWebException(String errorMessage, int statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
