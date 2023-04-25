package com.cmdb.integration.repository;

import com.cmdb.integration.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    default Product saveOrUpdate(Product product) {
        Optional<Product> existingProduct = findByName(product.getName());
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setId(product.getId());
            updatedProduct.setName(product.getName());
            updatedProduct.setCreatedAt(product.getCreatedAt());
            updatedProduct.setUpdatedAt(product.getUpdatedAt());
            return save(updatedProduct);
        } else {
            return save(product);
        }
    }
}
