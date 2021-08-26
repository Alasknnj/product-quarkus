package br.com.compasso.desafio.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

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
    // TODO: 8/25/21 add unique to name?
    String name;

    @Column(name = "description")

    @Size(max = 200, message = "description cannot be over 200 characters")
    @NotBlank(message = "description is mandatory")
    String description;

    @Positive(message = "price must be positive")
    @Column(name = "price")
    @NotNull(message = "price is mandatory")

    BigDecimal price;

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
