package com.Products.Dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Products.Model.product;

@Repository
public interface productRepository extends JpaRepository<product, Integer> {
	List<product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(String name, String description);
}
