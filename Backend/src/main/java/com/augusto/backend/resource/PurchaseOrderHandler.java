package com.augusto.backend.resource;

import com.augusto.backend.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PurchaseOrderHandler {

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderHandler(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    public Mono<ServerResponse> getClients(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> "Rest test"), String.class);
    }

    public Mono<ServerResponse> getPurchaseOrderById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> purchaseOrderService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(purchaseOrder -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(purchaseOrder))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(e));

    }
}
