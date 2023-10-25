package com.augusto.backend.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> categoryRouter(CategoryHandler categoryHandler) {
        return route().path("/categories", builder -> builder
                .nest(accept(MediaType.APPLICATION_JSON), uriBuilder -> uriBuilder
                        .GET("", categoryHandler::getCategories)
                        .POST("", categoryHandler::createCategory)
                        .PUT("/{id}", categoryHandler::updateCategory)
                        .DELETE("/{id}", categoryHandler::deleteCategoryById)
                        .GET("/{id}", categoryHandler::getCategoriesById)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> clientRouter(ClientHandler clientHandler) {
        return route().path("/clients", builder -> builder
                .nest(accept(MediaType.APPLICATION_JSON), uriBuilder -> uriBuilder
                        .GET("", clientHandler::getClients)
                        .GET("/{id}", clientHandler::getClientById)
                        .POST("", clientHandler::createClient)
                        .PUT("/{id}", clientHandler::updateClient)
                        .DELETE("/{id}", clientHandler::deleteClientById)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> purchaseOrderRouter(PurchaseOrderHandler purchaseOrderHandler) {
        return route().path("/purchase-orders", builder -> builder
                .nest(accept(MediaType.APPLICATION_JSON), uriBuilder -> uriBuilder
                        .GET("", purchaseOrderHandler::getPurchaseOrders)
                        .GET("/{id}", purchaseOrderHandler::getPurchaseOrderById))
                        .POST("", purchaseOrderHandler::createPurchaseOrder))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> productRouter(ProductHandler productHandler) {
        return route().path("/products/search", builder -> builder
                        .GET("", productHandler::searchProducts))
                .nest(RequestPredicates.path("/products"), uriBuilder -> uriBuilder
                        .GET("", productHandler::getProducts)
                        .GET("/{id}", productHandler::getProductsById))
                .build();
    }
}