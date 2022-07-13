package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Flux<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Mono<Category> findById(final Integer id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException("Category not found for Id: " + id)));
    }

    public Mono<Category> create(final CategoryDto categoryDto) {
        return categoryRepository.save(toDomainObject(categoryDto));
    }

    @Transactional
    public Mono<Category> update(final CategoryDto categoryDto) {
        return findById(categoryDto.getId())
                .flatMap(foundCategory -> {
                    foundCategory.setName(categoryDto.getName());
                    return categoryRepository.save(foundCategory);
                });
    }

    @Transactional
    public Mono<Integer> deleteById(final Integer id) {
        return categoryRepository.deleteById(id)
                .thenReturn(id);
    }

    private Category toDomainObject(final CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName(), List.of());
    }
}
