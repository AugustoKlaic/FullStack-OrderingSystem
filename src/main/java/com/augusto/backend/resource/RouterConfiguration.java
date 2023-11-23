package com.augusto.backend.resource;

import com.augusto.backend.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
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
                        .DELETE("/{id}", clientHandler::deleteClientById))
                .nest(contentType(MediaType.MULTIPART_FORM_DATA), profilePicBuilder -> profilePicBuilder
                        .POST("/profile-picture", clientHandler::uploadProfilePicture)))
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
    @RouterOperations({
            @RouterOperation(path = "/products/search",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = ProductHandler.class,
                    beanMethod = "searchProducts",
                    operation = @Operation(operationId = "searchProducts",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Product.class)))})),
            @RouterOperation(path = "/products/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = ProductHandler.class,
                    beanMethod = "getProductsById",
                    operation = @Operation(operationId = "getProductsById",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Product.class))),
                                    @ApiResponse(responseCode = "404",
                                            description = "Product not found")},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id")}))})
    public RouterFunction<ServerResponse> productRouter(ProductHandler productHandler) {
        return route().path("/products/search", builder -> builder
                        .GET("", productHandler::searchProducts))
                .nest(RequestPredicates.path("/products"), builder -> builder
                        .GET("/{id}", productHandler::getProductsById))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> securityRouter(SecurityHandler securityHandler) {
        return route().path("", builder -> builder
                .POST("/login", securityHandler::login))
                .POST("/token-refresh", securityHandler::refreshToken)
                .POST("/forgot-password", securityHandler::forgetPassword)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> addressRouter(AddressHandler addressHandler) {
        return route().path("/address", builder -> builder
                .nest(accept(MediaType.APPLICATION_JSON), citiesBuilder -> citiesBuilder
                        .GET("/state/{stateId}/cities", addressHandler::getCities))
                .nest(accept(MediaType.APPLICATION_JSON), statesBuilder -> statesBuilder
                        .GET("/states", addressHandler::getStates)))
                .build();
    }
}
