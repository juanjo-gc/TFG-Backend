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

    /*
    @Cacheable
    public List<Integer> getFilteredEventIdsPage(EventFilterDTO eventFilterDTO, int iPageNumber) {
        List<Event> aFilteredEvents = new ArrayList<>();
        List<Integer> aiFilteredByName = new ArrayList<>();
        List<Integer> aiFilteredByInterests = new ArrayList<>();
        List<Integer> aiFilteredByLocation = _eventRepository.findEventIdsByLocation(
                _provinceRepository.findBy_sName(eventFilterDTO.get_sProvince()),
                _regionRepository.findBy_sName(eventFilterDTO.get_sRegion()),
                _countryRepository.findBy_sName(eventFilterDTO.get_sCountry())
        );

        if(eventFilterDTO.get_sTitle() != null || eventFilterDTO.get_sTitle() != "") {
            aiFilteredByName = _eventRepository.findBy_sTitleLike(eventFilterDTO.get_sTitle());
        } else {
            //TODO Cambiar esto para devolver listas a partir de página con el pageNumber
            aiFilteredByName = _eventRepository.findAllEventIds();
        }

        if(!eventFilterDTO.get_asInterests().isEmpty()) {
            int iNumberOfInterests = eventFilterDTO.get_asInterests().size();
            aiFilteredByInterests = _eventRepository.findEventIdsByOptionalInterests(
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(2)) : null
            );
        }

        List<Integer> finalAiFilteredByInterests = aiFilteredByInterests;
        aiFilteredByName = aiFilteredByName.stream().filter(iId -> finalAiFilteredByInterests.contains(iId)).collect(Collectors.toList());
        aiFilteredByName = aiFilteredByName.stream().filter(iId -> aiFilteredByLocation.contains(iId)).collect(Collectors.toList());

        return Collections.emptyList();
    }

     */

}
