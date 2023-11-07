package com.augusto.backend.dto;

import com.augusto.backend.domain.Product;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class CategoryDto {

    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Length(min = 5, max = 80, message = "Length must be between 5 and 80 characters")
    private String name;

    private List<Product> products;

    public CategoryDto() {
    }

    public CategoryDto(Integer id, String name, List<Product> products) {
        this();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
