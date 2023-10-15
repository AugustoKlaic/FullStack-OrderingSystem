package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.domain.Product;
import com.augusto.backend.repository.CategoryRepository;
import com.augusto.backend.repository.ProductRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product findById(final Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Product not found for Id: " + id));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name, List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        return productRepository.search(name, categories);
    }

}
