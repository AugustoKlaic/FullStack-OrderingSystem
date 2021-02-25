package com.augusto.backend.service;

import com.augusto.backend.domain.PurchaseOrder;
import com.augusto.backend.repository.PurchaseOrderRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public PurchaseOrder findById(final Integer id) {
        return purchaseOrderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Purchase Order not found for Id: " + id));
    }

}
