package br.com.compasso.desafio.rest.resource;

import br.com.compasso.desafio.domain.entity.Product;
import br.com.compasso.desafio.rest.dto.ProductDTO;
import br.com.compasso.desafio.service.ProductService;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("products")
public class ProductResource {

    @Inject
    ProductService service;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // TODO: 8/25/21
    public Response create(@NotNull(message = "DTO is mandatory") final ProductDTO dto) {

        Product product = dto.toEntity();
        ProductDTO dto2 = new ProductDTO(service.create(product));
        return Response.status(HttpStatus.SC_CREATED).entity(dto2).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // TODO: 8/25/
    public ProductDTO update(
        @PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id,
        @NotNull(message = "DTO is mandatory") final ProductDTO dto) {

        return null;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // TODO: 8/25/21
    public ProductDTO getById(@PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id) {
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // TODO: 8/25/21
    public List<ProductDTO> getAll() {
        return null;
    }

    @GET
    @Path("search")
    // TODO: 8/25/21 check how to implement
    public List<ProductDTO> getByQuery() {
        return null;
    }

    @DELETE
    @Path("{id}")
    // TODO: 8/25/21
    public Response delete(@PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id) {
        return null;
    }
}
