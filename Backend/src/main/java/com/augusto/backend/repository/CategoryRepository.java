package com.augusto.backend.repository;

import com.augusto.backend.domain.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Integer> {

    @Query("select distinct c from Category c left join fetch c.productList")
    public Flux<Category> findAll();

    @Query("select c from Category c left join fetch c.productList where c.id = :id ")
    public Mono<Category> findById(@Param("id") Integer id);

    @Modifying
    @Query("delete from Category c where c.id = :id ")
    public Mono<Void> deleteById(@Param("id") Integer id);
}
