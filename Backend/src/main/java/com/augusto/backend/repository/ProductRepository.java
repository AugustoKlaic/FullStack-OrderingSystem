package com.augusto.backend.repository;

import com.augusto.backend.domain.Category;
import com.augusto.backend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("  select distinct product from Product product " +
            " inner join product.categoryList categories " +
            " where product.name like %:name% " +
            "   and categories in :categories")
    List<Product> search(@Param("name") String name, @Param("categories") List<Category> categories);
}