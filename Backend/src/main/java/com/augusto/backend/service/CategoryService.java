package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(final Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found for Id: " + id));
    }

    public Category create(final CategoryDto categoryDto) {
        return categoryRepository.save(toDomainObject(categoryDto));
    }

    @Transactional
    public Category update(final Category category) {
        final Category updateCategory = findById(category.getId());
        updateCategory.setName(category.getName());
        return categoryRepository.save(updateCategory);
    }

    @Transactional
    public Integer deleteById(final Integer id) {
        categoryRepository.deleteById(findById(id).getId());
        return id;
    }

    private Category toDomainObject(final CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName(), new ArrayList<>());
    }
}
