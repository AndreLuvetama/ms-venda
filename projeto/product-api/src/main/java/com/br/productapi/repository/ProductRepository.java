package com.br.productapi.repository;

import com.br.productapi.model.Category;
import com.br.productapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findByCategoryId(Integer id);
    List<Product> findBySupplierId(Integer id);

    Boolean existsByCategoryId(Integer id);
    Boolean existsBySupplierId(Integer id);
}
