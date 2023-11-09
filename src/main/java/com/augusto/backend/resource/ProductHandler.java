package com.augusto.backend.resource;

import com.augusto.backend.service.ProductService;
import com.augusto.backend.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductHandler {

    private static final String PRODUCT_DOMAIN = "Product";

    private final ProductService productService;

    @Autowired
    public ProductHandler(ProductService productService) {
        this.productService = productService;
    }

    public Mono<ServerResponse> getProductsById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> productService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, PRODUCT_DOMAIN));
    }

    public Mono<ServerResponse> searchProducts(ServerRequest serverRequest) {
        List<Integer> ids = Optional.ofNullable(serverRequest.queryParams().get("categories"))
                .orElse(List.of())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        String name = URLDecoder.decode(serverRequest.queryParam("name").orElse(""), StandardCharsets.UTF_8);

        return Mono.fromCallable(() -> productService.searchProducts(name, ids))
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, PRODUCT_DOMAIN));
    }
}
