package com.augusto.backend.resource;

import com.augusto.backend.resource.exception.WebException;
import com.augusto.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return Mono.fromCallable(() -> categoryService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(category -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(category))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(new WebException(HttpStatus.NOT_FOUND, e.getMessage())));

    }
}
