package br.com.compasso.desafio.domain.entity;

import br.com.compasso.desafio.service.exception.ProductNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

@Entity(name = "product")
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Size(max = 50, message = "name cannot be over 50 characters")
    @NotBlank(message = "name is mandatory")
    @Column(name = "name")
    String name;

    @Column(name = "description")

    @Size(max = 200, message = "description cannot be over 200 characters")
    @NotBlank(message = "description is mandatory")
    String description;

    @Positive(message = "price must be positive")
    @Column(name = "price")
    @NotNull(message = "price is mandatory")
    BigDecimal price;

    public static Product getProductByIdOrThrow(String id) {
        return (Product) findByIdOptional(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public static List<Product> getAllProducts() {
        return listAll();
    }

    public static List<Product> search(String textSearch, String minPrice, String maxPrice) {
        if(textSearch == null && minPrice == null && maxPrice == null) {
            return listAll();
        }

        String query = "";
        if(textSearch != null) {
            query = "(lower(name) || lower(description)) like '%" + textSearch.trim().toLowerCase() + "%'";
            query += minPrice != null ? " AND price >= " + minPrice : "";
            query += maxPrice != null ? " AND price <= " + maxPrice : "";
        } else if (minPrice != null && maxPrice != null) {
            query = "price >= " + minPrice + "AND price <= " + maxPrice;
        } else {
            query = minPrice != null ? "price >= " + minPrice : "price <= " + maxPrice;
        }

        return list(query);
    }

    public void update(@Valid Product updatedProduct) {
        setName(updatedProduct.getName());
        setDescription(updatedProduct.getDescription());
        setPrice(updatedProduct.getPrice());
    }

    public String getId() {
        return id;
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
