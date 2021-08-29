package br.com.compasso.desafio.rest.exceptionmapper;

import br.com.compasso.desafio.domain.exception.WebError;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {
    private static final String ERROR_MSG = "The value %s is not valid for type %s";

    @Override
    public Response toResponse(InvalidFormatException e) {
        return Response.status(HttpStatus.SC_BAD_REQUEST).entity(new WebError(String.format(ERROR_MSG, e.getValue(), e.getTargetType().toString()), HttpStatus.SC_BAD_REQUEST)).build();
    }
}
