package br.com.compasso.desafio.rest.resource;

import br.com.compasso.desafio.domain.entity.Product;
import br.com.compasso.desafio.rest.dto.ProductDTO;
import br.com.compasso.desafio.service.ProductService;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

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
    @RequestBody
    @Operation(description = "Create a new Product")
    @APIResponse(responseCode = "201", description = "Product created successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    public Response create(@NotNull(message = "DTO is mandatory") final ProductDTO dto) {
        return Response.status(HttpStatus.SC_CREATED)
                .entity(new ProductDTO(service.create(dto.toEntity()))).build();
    }

    @PUT
    @Path("{id}")
    @RequestBody
    @Operation(description = "Update an existing Product")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "400", description = "Product not found")
    })
    public ProductDTO update(
        @PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id,
        @NotNull(message = "DTO is mandatory") final ProductDTO dto) {

        return new ProductDTO(service.update(id, dto.toEntity()));
    }

    @GET
    @Path("{id}")
    @Operation(description = "Find an existing Product by ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "400", description = "Product not found")
    })
    public ProductDTO getById(@PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id) {
        return new ProductDTO(Product.getProductByIdOrThrow(id));
    }

    @GET
    @Operation(description = "List all existing Products")
    @APIResponse(responseCode = "200", description = "Listing Products",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class, type = SchemaType.ARRAY)))
    public List<ProductDTO> getAll() {
        return ProductDTO.toDTOList(Product.getAllProducts());
    }

    @GET
    @Path("search")
    @Operation(description = "Search Product by name/description and/or price range")
    @APIResponse(responseCode = "200", description = "Listing Products",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class, type = SchemaType.ARRAY)))
    public List<ProductDTO> getByQuery(
        @QueryParam("q") final String textSearch,
        @QueryParam("min_price") final String minPrice,
        @QueryParam("max_price") final String maxPrice) {

        return ProductDTO.toDTOList(Product.search(textSearch, minPrice, maxPrice));
    }

    @DELETE
    @Path("{id}")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Product deleted"),
        @APIResponse(responseCode = "400", description = "Product not found")
    })
    public Response delete(@PathParam("id") @Size(min = 36, max = 36, message = "ID must have 36 characters") final String id) {
        service.delete(id);
        return Response.ok().build();
    }
}
