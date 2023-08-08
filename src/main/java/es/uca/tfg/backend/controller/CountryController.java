package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private CountryRepository _countryRepository;

    @GetMapping("/getAllCountries")
    public List<Country> getCountryNames(){
        return _countryRepository.findAll();
    }
}
