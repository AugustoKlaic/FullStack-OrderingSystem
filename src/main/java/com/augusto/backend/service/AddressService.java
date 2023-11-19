package com.augusto.backend.service;

import com.augusto.backend.domain.City;
import com.augusto.backend.domain.State;
import com.augusto.backend.dto.CityDto;
import com.augusto.backend.dto.StateDto;
import com.augusto.backend.repository.CityRepository;
import com.augusto.backend.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    public AddressService(StateRepository stateRepository, CityRepository cityRepository) {
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
    }

    public List<StateDto> findStates() {
        return stateRepository.findAllStates().stream()
                .map(this::toDomainObject).collect(Collectors.toList());
    }

    public List<CityDto> findCitiesByState(Integer stateId) {
        return cityRepository.findAllCitiesByState(stateId).stream()
                .map(this::toDomainObject).collect(Collectors.toList());
    }

    private StateDto toDomainObject(State state) {
        return new StateDto(state);
    }

    private CityDto toDomainObject(City city) {
        return new CityDto(city);
    }
}
