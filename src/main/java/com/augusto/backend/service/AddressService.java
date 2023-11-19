package com.augusto.backend.service;

import com.augusto.backend.domain.City;
import com.augusto.backend.domain.State;
import com.augusto.backend.repository.CityRepository;
import com.augusto.backend.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    public AddressService(StateRepository stateRepository, CityRepository cityRepository) {
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
    }

    public List<State> findStates() {
        return stateRepository.findAllStates();
    }

    public List<City> findCitiesByState(Integer stateId) {
        return cityRepository.findAllCitiesByState(stateId);
    }
}
