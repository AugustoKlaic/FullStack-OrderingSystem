package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }
}
