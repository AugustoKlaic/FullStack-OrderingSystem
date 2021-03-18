package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName())).collect(Collectors.toList());
    }

    public Category findById(final Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found for Id: " + id));
    }

    public Category create(final Category category) {
        return categoryRepository.save(category);
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
}
