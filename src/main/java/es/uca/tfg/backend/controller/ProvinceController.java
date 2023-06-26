package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.CountryRepository;
import es.uca.tfg.backend.repository.ProvinceRepository;
import es.uca.tfg.backend.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class ProvinceController {

    @Autowired
    ProvinceRepository _provinceRepository;

    @Autowired
    RegionRepository _regionRepository;

    @Autowired
    CountryRepository _countryRepository;

    @GetMapping("/getCountryRegions/{countryId}")
    public List<Region> getCountryRegions(@PathVariable("countryId") int iCountryId) {
        Optional<Country> optionalCountry = _countryRepository.findById(iCountryId);
        return optionalCountry.isPresent() ? optionalCountry.get().get_setRegions().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/getRegionProvinces/{regionId}")
    public List<Province> getRegionProvinces(@PathVariable("regionId") int iRegionId) {
        Optional<Region> optionalRegion = _regionRepository.findById(iRegionId);
        return optionalRegion.isPresent() ? optionalRegion.get().get_setProvinces().stream().toList() : Collections.emptyList();
    }
}
