package com.augusto.backend.repository;

import com.augusto.backend.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select c from Client c join fetch c.addresses join fetch c.telephones where c.id = :id ")
    public Optional<Client> findById(@Param("id") Integer id);

    @Query("select distinct c from Client c join fetch c.addresses join fetch c.telephones")
    public List<Client> findAll();
}
