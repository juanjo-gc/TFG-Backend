package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.ProvinceDTO;
import es.uca.tfg.backend.rest.RegionDTO;
import es.uca.tfg.backend.service.ProvinceService;
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
    private ProvinceRepository _provinceRepository;
    @Autowired
    private RegionRepository _regionRepository;
    @Autowired
    private CountryRepository _countryRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private LocationRepository _locationRepository;
    @Autowired
    private ProvinceService _provinceService;

    @GetMapping("/getAllProvinces")
    public List<Province> getAllProvinces() {
        return _provinceRepository.findAll();
    }

    @GetMapping("/getRegionProvinces/{regionId}")
    public List<Province> getRegionProvinces(@PathVariable("regionId") int iRegionId) {
        Optional<Region> optionalRegion = _regionRepository.findById(iRegionId);
        return optionalRegion.isPresent() ? optionalRegion.get().get_setProvinces().stream().toList() : Collections.emptyList();
    }

    @PostMapping("/createProvince")
    public Province createProvince(@RequestBody ProvinceDTO provinceDTO) {
        Optional<Region> optionalRegion = _regionRepository.findBy_sName(provinceDTO.get_sRegion());
        if(optionalRegion.isPresent())
            return _provinceRepository.save(new Province(provinceDTO.get_sName(), optionalRegion.get()));
        else
            return new Province();
    }


    @PatchMapping("/updateProvince/{id}")
    public Province updateProvince(@RequestBody ProvinceDTO provinceDTO, @PathVariable("id") int iProvinceId) {
        Optional<Province> optionalProvince = _provinceRepository.findById(iProvinceId);
        Optional<Region> optionalRegion = _regionRepository.findBy_sName(provinceDTO.get_sRegion());
        if(optionalProvince.isPresent() && optionalRegion.isPresent()) {
            Province province = optionalProvince.get();
            province.set_sName(provinceDTO.get_sName());
            province.set_region(optionalRegion.get());
            return _provinceRepository.save(province);
        } else {
            return new Province();
        }
    }

    @GetMapping("/getUserProvince/{userId}")
    public Province getUserProvince(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_province() : new Province();
    }

    @DeleteMapping("/deleteProvince/{id}")
    public void deleteProvince(@PathVariable("id") int iProvinceId) {
        _provinceService.delete(iProvinceId);
    }
}
