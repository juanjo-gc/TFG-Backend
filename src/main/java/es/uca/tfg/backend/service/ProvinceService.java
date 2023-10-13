package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Location;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.LocationRepository;
import es.uca.tfg.backend.repository.ProvinceRepository;
import es.uca.tfg.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ProvinceService {

    private final ProvinceRepository _provinceRepository;

    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private LocationRepository _locationRepository;

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

    public void delete(int iProvinceId) {
        Optional<Province> optionalProvince = _provinceRepository.findById(iProvinceId);
        if(optionalProvince.isPresent()) {
            List<User> usersInProvince = _userRepository.findBy_province(optionalProvince.get());
            List<Location> locationsInProvince = _locationRepository.findBy_province(optionalProvince.get());
            for(User user: usersInProvince) {
                user.set_province(null);
            }
            for(Location location: locationsInProvince) {
                location.set_province(null);
            }
            _provinceRepository.deleteById(iProvinceId);
        }
    }

    public int count() {
        return (int) _provinceRepository.count();
    }


}
