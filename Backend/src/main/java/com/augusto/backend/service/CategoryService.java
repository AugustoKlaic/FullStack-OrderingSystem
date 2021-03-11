package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(final Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Category not found for Id: " + id));
    }

    public Category create(final Category category) {
        return categoryRepository.save(category);
    }

    public Category update(final Category category) {
        final Category updateCategory = findById(category.getId());
        updateCategory.setName(category.getName());
        updateCategory.setProductList(category.getProductList());
        return categoryRepository.save(updateCategory);
    }
}
