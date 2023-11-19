package com.augusto.backend.dto;

import com.augusto.backend.domain.City;

public class CityDto {

    private Integer id;
    private String name;

    public CityDto(Integer id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public CityDto(City city) {
        this();
        this.id = city.getId();
        this.name = city.getName();
    }

    public CityDto() {
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
