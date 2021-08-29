package br.com.compasso.desafio.domain.exception;

/**
 * Abstract class for all Web Application Errors the service throws.
 */
public abstract class BaseWebException extends RuntimeException {
    private final int statusCode;

    protected BaseWebException(String errorMessage, int statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
