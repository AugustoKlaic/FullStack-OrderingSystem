package com.augusto.backend.resource;

import com.augusto.backend.domain.Category;
import com.augusto.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CategoryHandler {

    private final CategoryService categoryService;

    @Autowired
    public CategoryHandler(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Mono<ServerResponse> getCategories(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> "Rest test"), String.class);
    }

    public Mono<ServerResponse> getCategoriesById(ServerRequest serverRequest) {
        Integer id = Integer.parseInt(serverRequest.pathVariable("id"));


        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() ->
                        categoryService.findById(id)), Category.class);
    }
}
