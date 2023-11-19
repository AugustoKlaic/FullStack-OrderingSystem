package com.augusto.backend.dto;

import com.augusto.backend.domain.State;

public class StateDto {

    private Integer id;
    private String name;

    public StateDto(Integer id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public StateDto(State state) {
        this();
        this.id = state.getId();
        this.name = state.getName();
    }

    public StateDto() {
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
