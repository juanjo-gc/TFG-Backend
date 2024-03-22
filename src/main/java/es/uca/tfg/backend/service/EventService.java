package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.EventFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository _eventRepository;
    @Autowired
    InterestRepository _interestRepository;
    @Autowired
    ProvinceRepository _provinceRepository;
    @Autowired
    RegionRepository _regionRepository;
    @Autowired
    CountryRepository _countryRepository;

    @Cacheable
    public Page<Event> findAll(Pageable pageable) {
        return _eventRepository.findAll(PageRequest.of(0, 10, Sort.by("_tCelebratedAt").ascending()));
    }

}
