package com.augusto.backend.resource;

import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.resource.validator.ErrorClass;
import com.augusto.backend.resource.validator.RequestValidator;
import com.augusto.backend.resource.validator.ValidatorException;
import com.augusto.backend.service.CategoryService;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CategoryHandler {

    private static final String CATEGORY_URI = "/categories/";
    private final CategoryService categoryService;
    private final RequestValidator requestValidator;

    @Autowired
    public CategoryHandler(CategoryService categoryService, RequestValidator requestValidator) {
        this.categoryService = categoryService;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> getCategories(ServerRequest serverRequest) {
        return Mono.fromCallable(categoryService::findAllCategories)
                .flatMap(categories -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(categories));
    }

    public Mono<ServerResponse> getCategoriesById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> categoryService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(category -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(category))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> createCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CategoryDto.class)
                .doOnNext(requestValidator::validateRequest)
                .map(categoryService::create)
                .flatMap(createdCategory -> ServerResponse.created(
                        URI.create(CATEGORY_URI.concat(String.valueOf(createdCategory.getId()))))
                        .bodyValue(createdCategory))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> updateCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CategoryDto.class)
                .doOnNext(requestValidator::validateRequest)
                .map(categoryService::update)
                .flatMap(updatedCategory -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(updatedCategory))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> deleteCategoryById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> categoryService.deleteById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(categoryId -> ServerResponse.ok().bodyValue(categoryId))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> errorHandler(Throwable error) {
        if (error instanceof ValidatorException) {
            return ServerResponse.unprocessableEntity()
                    .bodyValue(new ErrorClass(((ValidatorException) error).getErrorDetail()));
        } else if (error instanceof ObjectNotFoundException) {
            return ServerResponse.notFound().build();
        }
        else {
            return ServerResponse.badRequest().build();
        }
    }
}
