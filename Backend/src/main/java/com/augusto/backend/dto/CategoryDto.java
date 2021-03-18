package com.augusto.backend.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class CategoryDto {

    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Length(min = 5, max = 80, message = "Length must be between 5 and 80 characters")
    private String name;

    public CategoryDto() {
    }

    public CategoryDto(Integer id, String name) {
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
}
