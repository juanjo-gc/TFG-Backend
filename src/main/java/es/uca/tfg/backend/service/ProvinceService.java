package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ProvinceService {

    private final ProvinceRepository _provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository _provinceRepository) {
        this._provinceRepository = _provinceRepository;
    }

    public Optional<Province> get(int id) {
        return _provinceRepository.findById(id);
    }

    public Province update(Province entity) {
        return _provinceRepository.save(entity);
    }

    public void delete(int id) {
        _provinceRepository.deleteById(id);
    }

    public int count() {
        return (int) _provinceRepository.count();
    }
}
