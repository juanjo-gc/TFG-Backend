package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class CountryService {

    private final CountryRepository _countryRepository;

    @Autowired
    public CountryService(CountryRepository _countryRepository) {
        this._countryRepository = _countryRepository;
    }

    public Optional<Country> get(int id) {
        return _countryRepository.findById(id);
    }

    public Country update(Country entity) {
        return _countryRepository.save(entity);
    }

    public void delete(int id) {
        _countryRepository.deleteById(id);
    }

    public int count() {
        return (int) _countryRepository.count();
    }
}
