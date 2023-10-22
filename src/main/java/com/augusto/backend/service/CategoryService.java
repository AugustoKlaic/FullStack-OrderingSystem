package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(final Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found for Id: " + id));
    }

    @Transactional
    public Category create(final CategoryDto categoryDto) {
        return categoryRepository.save(toDomainObject(categoryDto));
    }

    @Transactional
    public Category update(final CategoryDto categoryDto) {
        final Category toUpdateCategory = findById(categoryDto.getId());
        toUpdateCategory.setName(categoryDto.getName());
        return categoryRepository.save(toUpdateCategory);
    }

    @Transactional
    public Integer deleteById(final Integer id) {
        categoryRepository.deleteById(findById(id).getId());
        return id;
    }

    private Category toDomainObject(final CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName(), List.of());
    }
}
