package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class RegionService {

    private final RegionRepository _regionRepository;

    @Autowired
    public RegionService(RegionRepository _regionRepository) {
        this._regionRepository = _regionRepository;
    }

    public Optional<Region> get(int id) {
        return _regionRepository.findById(id);
    }

    public Region update(Region entity) {
        return _regionRepository.save(entity);
    }

    public void delete(int id) {
        _regionRepository.deleteById(id);
    }

    public int count() {
        return (int) _regionRepository.count();
    }
}
