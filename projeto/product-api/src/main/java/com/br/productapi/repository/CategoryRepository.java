package com.br.productapi.repository;

import com.br.productapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByDescriptionIgnoreCaseContaining(String description);
    //List<Category> findByDescription(String description);
}
