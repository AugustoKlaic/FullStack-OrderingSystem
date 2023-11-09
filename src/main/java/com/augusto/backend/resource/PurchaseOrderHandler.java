package com.augusto.backend.resource;

import com.augusto.backend.domain.PurchaseOrder;
import com.augusto.backend.resource.validator.ErrorClass;
import com.augusto.backend.resource.validator.ValidatorException;
import com.augusto.backend.service.PurchaseOrderService;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class PurchaseOrderHandler {
    private static final String PURCHASE_ORDERS_URI = "/purchase-orders";
    private static final String PURCHASE_ORDERS_DOMAIN = "Purchase-Order";

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderHandler(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    public Mono<ServerResponse> getPurchaseOrders(ServerRequest serverRequest) {
        return serverRequest.principal()
                .map(user -> purchaseOrderService.findAllPurchaseOrders(user.getName()))
                .flatMap(purchaseOrder -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(purchaseOrder))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, PURCHASE_ORDERS_DOMAIN));
    }

    public Mono<ServerResponse> getPurchaseOrderById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> purchaseOrderService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(purchaseOrder -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(purchaseOrder))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, PURCHASE_ORDERS_DOMAIN));
    }

    public Mono<ServerResponse> createPurchaseOrder(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PurchaseOrder.class)
                .map(purchaseOrderService::create)
                .flatMap(createdPurchaseOrder -> ServerResponse.created(
                                URI.create(PURCHASE_ORDERS_URI.concat(String.valueOf(createdPurchaseOrder.getId()))))
                        .bodyValue(createdPurchaseOrder))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, PURCHASE_ORDERS_DOMAIN));
    }
}
