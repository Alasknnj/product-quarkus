package br.com.compasso.desafio.rest.exceptionmapper;

import br.com.compasso.desafio.domain.exception.WebError;
import org.apache.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

/**
 * Exception Mapper for all ConstraintViolations caused by javax validation violations on DTOs
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final int STATUS_CODE = HttpStatus.SC_BAD_REQUEST;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        WebError error = new WebError(errors, STATUS_CODE);
        return Response.status(STATUS_CODE).entity(error).build();
    }
}
