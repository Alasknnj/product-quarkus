package br.com.compasso.desafio.service.exception;

import br.com.compasso.desafio.domain.exception.BaseWebException;
import org.apache.http.HttpStatus;

/**
 * Exception thrown when the service cannot locate a Product by its ID
 */
public class ProductNotFoundException extends BaseWebException {
    private static final String ERROR_MSG = "The product with id %s was not found";

    public ProductNotFoundException(String id) {
        super(String.format(ERROR_MSG, id), HttpStatus.SC_NOT_FOUND);
    }
}
