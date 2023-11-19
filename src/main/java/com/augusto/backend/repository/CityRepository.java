package com.augusto.backend.repository;

import com.augusto.backend.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    @Query(" select c from City c join State s on s.id = c.state.id where s.id = :id order by c.name asc ")
    public List<City> findAllCitiesByState(@Param("id") Integer stateId);
}
