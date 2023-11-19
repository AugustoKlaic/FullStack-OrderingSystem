package com.augusto.backend.repository;

import com.augusto.backend.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

    @Query("select s from State s order by s.name asc")
    public List<State> findAllStates();
}
