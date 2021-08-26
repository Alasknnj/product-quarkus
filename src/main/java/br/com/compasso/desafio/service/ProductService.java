package br.com.compasso.desafio.service;

import br.com.compasso.desafio.domain.entity.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

@ApplicationScoped
public class ProductService {

    @Transactional
    public Product create(@Valid Product product) {
        product.persist();
        return product;
    }
}
