package com.augusto.backend.resource;

import com.augusto.backend.domain.Category;
import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.resource.exception.WebException;
import com.augusto.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CategoryHandler {

    private static final String CATEGORY_URI = "/categories/";
    private final CategoryService categoryService;
    private final RequestValidationHandler requestValidationHandler;

    @Autowired
    public CategoryHandler(CategoryService categoryService, RequestValidationHandler requestValidationHandler) {
        this.categoryService = categoryService;
        this.requestValidationHandler = requestValidationHandler;
    }

    public Mono<ServerResponse> getCategories(ServerRequest serverRequest) {
        return Mono.fromCallable(categoryService::findAllCategories)
                .flatMap(categories -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(categories));
    }

    public Mono<ServerResponse> getCategoriesById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> categoryService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(category -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(category))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(new WebException(HttpStatus.NOT_FOUND, e.getMessage())));

    }

    public Mono<ServerResponse> createCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CategoryDto.class)
                .map(requestValidationHandler::requireValidBody)
                .flatMap(createdCategory -> ServerResponse.created(URI.create(CATEGORY_URI.concat(String.valueOf(createdCategory.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(createdCategory));
    }

    public Mono<ServerResponse> updateCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Category.class).map(categoryService::update)
                .flatMap(updatedCategory -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updatedCategory))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(new WebException(HttpStatus.NOT_FOUND, e.getMessage())));
    }

    public Mono<ServerResponse> deleteCategoryById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> categoryService.deleteById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(categoryId -> ServerResponse.ok().bodyValue(categoryId))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(new WebException(HttpStatus.NOT_FOUND, e.getMessage())));
    }
}
