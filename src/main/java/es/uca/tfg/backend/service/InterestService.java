package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class InterestService {

    private final InterestRepository _interestRepository;

    @Autowired
    public InterestService(InterestRepository _interestRepository) {
        this._interestRepository = _interestRepository;
    }

    public Optional<Interest> get(int id) {
        return _interestRepository.findById(id);
    }

    public Interest update(Interest entity) {
        return _interestRepository.save(entity);
    }

    public void delete(int id) {
        _interestRepository.deleteById(id);
    }

    public int count() {
        return (int) _interestRepository.count();
    }
}
