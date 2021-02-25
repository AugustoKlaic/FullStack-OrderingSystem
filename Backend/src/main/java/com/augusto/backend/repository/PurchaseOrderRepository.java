package com.augusto.backend.repository;

import com.augusto.backend.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    @Query("select po from PurchaseOrder po" +
            " join fetch po.items " +
            " join fetch po.client c" +
            " join fetch c.addresses " +
            " join fetch c.telephones " +
            " where po.id = :id")
    public Optional<PurchaseOrder> findById(@Param("id") Integer id);
}
