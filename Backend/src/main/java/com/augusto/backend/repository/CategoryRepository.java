package com.augusto.backend.repository;

import com.augusto.backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c from Category c left join fetch c.productList where c.id = :id ")
    public Optional<Category> findById(@Param("id") Integer id);

    @Modifying
    @Query("delete from Category c where c.id = :id ")
    public void deleteById(@Param("id") Integer id);
}
