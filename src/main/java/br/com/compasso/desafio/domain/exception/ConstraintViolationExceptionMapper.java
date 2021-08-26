package br.com.compasso.desafio.domain.exception;

import org.apache.http.HttpStatus;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final int STATUS_CODE = HttpStatus.SC_BAD_REQUEST;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.joining(", "));;
        WebError error = new WebError(errors, STATUS_CODE);
        return Response.status(STATUS_CODE).entity(error).build();
    }
}
