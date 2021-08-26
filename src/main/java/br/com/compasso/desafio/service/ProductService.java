package br.com.compasso.desafio.service;

import br.com.compasso.desafio.domain.entity.Product;
import br.com.compasso.desafio.service.exception.ProductNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

@ApplicationScoped
public class ProductService {

    @Transactional
    public Product create(final @Valid Product product) {
        product.persist();
        return product;
    }

    @Transactional
    public Product update(final String id, final @Valid Product updatedProduct) {
        Product product = Product.getProductByIdOrThrow(id);
        product.update(updatedProduct);
        product.persist();
        return product;
    }

    @Transactional
    public void delete(final String id) {
        Product product = Product.getProductByIdOrThrow(id);
        product.delete();
    }
}
