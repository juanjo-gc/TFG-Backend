package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.CountryRepository;
import es.uca.tfg.backend.repository.RegionRepository;
import es.uca.tfg.backend.rest.RegionDTO;
import es.uca.tfg.backend.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class RegionController {

    @Autowired
    private RegionRepository _regionRepository;

    @Autowired
    private CountryRepository _countryRepository;
    @Autowired
    private RegionService _regionService;

    @GetMapping("/getAllRegions")
    public List<Region> getAllRegions() {
        return _regionRepository.findAll();
    }

    @GetMapping("/getCountryRegions/{countryId}")
    public List<Region> getCountryRegions(@PathVariable("countryId") int iCountryId) {
        Optional<Country> optionalCountry = _countryRepository.findById(iCountryId);
        return optionalCountry.isPresent() ? optionalCountry.get().get_setRegions().stream().toList() : Collections.emptyList();
    }

    @PostMapping("/createRegion")
    public Region createRegion(@RequestBody RegionDTO regionDTO) {
        Optional<Country> optionalCountry = _countryRepository.findBy_sName(regionDTO.get_sCountry());
        if(optionalCountry.isPresent())
            return _regionRepository.save(new Region(regionDTO.get_sName(), optionalCountry.get()));
        else
            return new Region();
    }

    @PatchMapping("/updateRegion/{id}")
    public Region updateRegion(@RequestBody RegionDTO regionDTO, @PathVariable("id") int iRegionId) {
        Optional<Region> optionalRegion = _regionRepository.findById(iRegionId);
        Optional<Country> optionalCountry = _countryRepository.findBy_sName(regionDTO.get_sCountry());
        if(optionalRegion.isPresent() && optionalCountry.isPresent()) {
            Region region = optionalRegion.get();
            region.set_sName(regionDTO.get_sName());
            region.set_country(optionalCountry.get());
            return _regionRepository.save(region);
        } else {
            return new Region();
        }
    }

    @DeleteMapping("/deleteRegion/{id}")
    public void deleteRegion(@PathVariable("id") int iRegionId) {
        _regionService.delete(iRegionId);
    }

}
