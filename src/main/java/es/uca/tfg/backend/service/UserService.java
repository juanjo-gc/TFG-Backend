package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository _userRepository;

    @Autowired
    public UserService(UserRepository _userRepository) {
        this._userRepository = _userRepository;
    }

    public Optional<User> get(int id) {
        return _userRepository.findById(id);
    }

    public User update(User entity) {
        return _userRepository.save(entity);
    }

    public void delete(int id) {
        _userRepository.deleteById(id);
    }

    public int count() {
        return (int) _userRepository.count();
    }

}
