package com.augusto.backend.resource;

import com.augusto.backend.domain.Category;
import com.augusto.backend.domain.Client;
import com.augusto.backend.domain.Product;
import com.augusto.backend.domain.PurchaseOrder;
import com.augusto.backend.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
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
    @RouterOperations({
            @RouterOperation(path = "/categories",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = CategoryHandler.class,
                    beanMethod = "getCategories",
                    operation = @Operation(operationId = "getCategories",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Category.class)))})),
            @RouterOperation(path = "/categories",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = CategoryHandler.class,
                    beanMethod = "createCategory",
                    operation = @Operation(operationId = "createCategory",
                            responses = {@ApiResponse(responseCode = "201",
                                    description = "Created",
                                    content = @Content(schema = @Schema(implementation = Category.class)))},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryDto.class))))),
            @RouterOperation(path = "/categories/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = CategoryHandler.class,
                    beanMethod = "getCategoriesById",
                    operation = @Operation(operationId = "getCategoriesById",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Category.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)})),
            @RouterOperation(path = "/categories/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = CategoryHandler.class,
                    beanMethod = "updateCategory",
                    operation = @Operation(operationId = "updateCategory",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Updated",
                                    content = @Content(schema = @Schema(implementation = Category.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryDto.class))))),
            @RouterOperation(path = "/categories/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.DELETE,
                    beanClass = CategoryHandler.class,
                    beanMethod = "deleteCategoryById",
                    operation = @Operation(operationId = "deleteCategoryById",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Deleted",
                                    content = @Content(schema = @Schema(implementation = Integer.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)}))})
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
    @RouterOperations({
            @RouterOperation(path = "/clients",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = ClientHandler.class,
                    beanMethod = "getClients",
                    operation = @Operation(operationId = "getClients",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Client.class)))},
                            parameters = {@Parameter(in = ParameterIn.QUERY, name = "email", required = false)})),
            @RouterOperation(path = "/clients",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = ClientHandler.class,
                    beanMethod = "createClient",
                    operation = @Operation(operationId = "createClient",
                            responses = {@ApiResponse(responseCode = "201",
                                    description = "Created",
                                    content = @Content(schema = @Schema(implementation = Client.class)))},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CompleteClientDto.class))))),
            @RouterOperation(path = "/clients/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = ClientHandler.class,
                    beanMethod = "getClientById",
                    operation = @Operation(operationId = "getClientById",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Client.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)})),
            @RouterOperation(path = "/clients/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = ClientHandler.class,
                    beanMethod = "updateClient",
                    operation = @Operation(operationId = "updateClient",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = Client.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ClientDto.class))))),
            @RouterOperation(path = "/clients/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.DELETE,
                    beanClass = ClientHandler.class,
                    beanMethod = "deleteClientById",
                    operation = @Operation(operationId = "deleteClientById",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Deleted",
                                    content = @Content(schema = @Schema(implementation = Integer.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)})),
            @RouterOperation(path = "/clients/profile-picture",
                    produces = MediaType.MULTIPART_FORM_DATA_VALUE,
                    method = RequestMethod.POST,
                    beanClass = ClientHandler.class,
                    beanMethod = "uploadProfilePicture",
                    operation = @Operation(operationId = "uploadProfilePicture",
                            responses = {@ApiResponse(responseCode = "201",
                                    description = "Profile picture Uploaded",
                                    content = @Content(schema = @Schema()))},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = FilePart.class)))))})
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
    @RouterOperations({
            @RouterOperation(path = "/purchase-orders",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = PurchaseOrderHandler.class,
                    beanMethod = "getPurchaseOrders",
                    operation = @Operation(operationId = "getPurchaseOrders",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = PurchaseOrder.class)))})),
            @RouterOperation(path = "/purchase-orders/products/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = PurchaseOrderHandler.class,
                    beanMethod = "getPurchaseOrderById",
                    operation = @Operation(operationId = "getPurchaseOrderById",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = PurchaseOrder.class))),
                                    @ApiResponse(responseCode = "404",
                                            description = "Purchase Order not found")},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)})),
            @RouterOperation(path = "/purchase-orders",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = PurchaseOrderHandler.class,
                    beanMethod = "createPurchaseOrder",
                    operation = @Operation(operationId = "createPurchaseOrder",
                            responses = {@ApiResponse(responseCode = "201",
                                    description = "Created",
                                    content = @Content(schema = @Schema(implementation = PurchaseOrder.class)))},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = PurchaseOrder.class)))))})
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
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", required = true)}))})
    public RouterFunction<ServerResponse> productRouter(ProductHandler productHandler) {
        return route().path("/products/search", builder -> builder
                        .GET("", productHandler::searchProducts))
                .nest(RequestPredicates.path("/products"), builder -> builder
                        .GET("/{id}", productHandler::getProductsById))
                .build();
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = SecurityHandler.class,
                    beanMethod = "login",
                    operation = @Operation(operationId = "login",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = TokenDto.class)))},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CredentialsDto.class))))),
            @RouterOperation(path = "/token-refresh",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = SecurityHandler.class,
                    beanMethod = "refreshToken",
                    operation = @Operation(operationId = "refreshToken",
                            responses = {@ApiResponse(responseCode = "204",
                                    description = " OK No content",
                                    content = @Content(schema = @Schema()))})),
            @RouterOperation(path = "/forgot-password",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = SecurityHandler.class,
                    beanMethod = "forgetPassword",
                    operation = @Operation(operationId = "forgetPassword",
                            responses = {@ApiResponse(responseCode = "204",
                                    description = "OK No content",
                                    content = @Content(schema = @Schema()))},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ForgotMyPasswordDto.class)))))})
    public RouterFunction<ServerResponse> securityRouter(SecurityHandler securityHandler) {
        return route().path("", builder -> builder
                        .POST("/login", securityHandler::login))
                .POST("/token-refresh", securityHandler::refreshToken)
                .POST("/forgot-password", securityHandler::forgetPassword)
                .build();
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/address/states",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = AddressHandler.class,
                    beanMethod = "getStates",
                    operation = @Operation(operationId = "getStates",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = StateDto.class)))})),
            @RouterOperation(path = "/address/state/{stateId}/cities",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = AddressHandler.class,
                    beanMethod = "getCities",
                    operation = @Operation(operationId = "getCities",
                            responses = {@ApiResponse(responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = CityDto.class)))},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "stateId", required = true)}))})
    public RouterFunction<ServerResponse> addressRouter(AddressHandler addressHandler) {
        return route().path("/address", builder -> builder
                        .nest(accept(MediaType.APPLICATION_JSON), citiesBuilder -> citiesBuilder
                                .GET("/state/{stateId}/cities", addressHandler::getCities))
                        .nest(accept(MediaType.APPLICATION_JSON), statesBuilder -> statesBuilder
                                .GET("/states", addressHandler::getStates)))
                .build();
    }
}
