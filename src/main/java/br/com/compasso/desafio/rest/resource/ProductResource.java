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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @POST
    // TODO: 8/25/21
    public Response create(@NotNull(message = "DTO is mandatory") final ProductDTO dto) {
        return Response.status(HttpStatus.SC_CREATED)
                .entity(new ProductDTO(service.create(dto.toEntity()))).build();
    }

    @PUT
    @Path("{id}")
    // TODO: 8/25/
    public ProductDTO update(
        @PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id,
        @NotNull(message = "DTO is mandatory") final ProductDTO dto) {

        return new ProductDTO(service.update(id, dto.toEntity()));
    }

    @GET
    @Path("{id}")
    // TODO: 8/25/21
    public ProductDTO getById(@PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id) {
        return new ProductDTO(Product.getProductByIdOrThrow(id));
    }

    @GET
    // TODO: 8/25/21
    public List<ProductDTO> getAll() {
        return ProductDTO.toDTOList(Product.getAllProducts());
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
        service.delete(id);
        return Response.ok().build();
    }
}
