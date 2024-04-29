package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.CountryRepository;
import es.uca.tfg.backend.rest.CountryDTO;
import es.uca.tfg.backend.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private CountryRepository _countryRepository;
    @Autowired
    private RegionService _regionService;

    @GetMapping("/getAllCountries")
    public List<Country> getCountryNames(){
        return _countryRepository.findAll();
    }

    @PostMapping("/createCountry")
    public Country createCountry(@RequestBody CountryDTO countryDTO) {
        return _countryRepository.save(new Country(countryDTO.get_sName()));
    }

    @PatchMapping("/updateCountry/{id}")
    public Country updateCountry(@RequestBody CountryDTO countryDTO, @PathVariable("id") int iCountryId) {
        Optional<Country> optionalCountry = _countryRepository.findById(iCountryId);
        if(optionalCountry.isPresent()) {
            Country country = optionalCountry.get();
            country.set_sName(countryDTO.get_sName());
            return _countryRepository.save(country);
        } else {
            return new Country();
        }
    }

    @DeleteMapping("/deleteCountry/{id}")
    public void deleteCountry(@PathVariable("id") int iCountryId) {
        Optional<Country> optionalCountry = _countryRepository.findById(iCountryId);
        if(optionalCountry.isPresent()) {
            for(Region region: optionalCountry.get().get_setRegions()) {
                _regionService.delete(region.get_iId());
            }
            _countryRepository.deleteById(iCountryId);
        }
    }

}
