package br.com.compasso.desafio.rest.dto;

import br.com.compasso.desafio.domain.entity.Product;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object used for creating and updating products, as well as forwarding Product information on GET requests
 */
public class ProductDTO {

    @Schema(readOnly = true)
    String id;

    String name;

    String description;

    BigDecimal price;

    public ProductDTO() {
        // Empty deserialization constructor
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public Product toEntity() {
        Product product = new Product();
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);

        return product;
    }

    public static List<ProductDTO> toDTOList(List<Product> productList) {
        return productList.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
