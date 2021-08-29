package br.com.compasso.desafio.rest.exceptionmapper;

import br.com.compasso.desafio.domain.exception.BaseWebException;
import br.com.compasso.desafio.domain.exception.WebError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception Mapper for Web Errors created and thrown by the service
 */
@Provider
public class BaseWebExceptionMapper implements ExceptionMapper<BaseWebException> {
    @Override
    public Response toResponse(BaseWebException e) {
        return Response.status(e.getStatusCode()).entity(new WebError(e.getMessage(), e.getStatusCode())).build();
    }
}
