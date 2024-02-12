package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service

public class RegionService {

    private final RegionRepository _regionRepository;
    @Autowired
    private ProvinceService _provinceService;

    @Autowired
    public RegionService(RegionRepository _regionRepository) {
        this._regionRepository = _regionRepository;
    }

    public Optional<Region> get(int id) { return _regionRepository.findById(id); }

    public Region update(Region entity) {
        return _regionRepository.save(entity);
    }

    public void delete(int iRegionId) {
        Optional<Region> optionalRegion = _regionRepository.findById(iRegionId);
        if(optionalRegion.isPresent()) {
            for (Province province: optionalRegion.get().get_setProvinces()) {
                _provinceService.delete(province.get_iId());
            }
            optionalRegion.get().set_setProvinces(new HashSet<>());
            _regionRepository.deleteById(iRegionId);
        }
    }

    public int count() {
        return (int) _regionRepository.count();
    }
}
