package es.uca.tfg.backend.service;

import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository _personRepository;
    private final UserRepository _userRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, UserRepository userRepository) {
        _personRepository = personRepository;
        _userRepository = userRepository;
    }

    public Optional<Person> get(int id) {
        return _personRepository.findById(id);
    }

    public Person update(Person entity) {
        return _personRepository.save(entity);
    }

    public void delete(int id) {
        _personRepository.deleteById(id);
    }

    public int count() {
        return (int) _personRepository.count();
    }

    public Person authenticate(String sEmail, String sPassword) {
        Person person  = _personRepository.findBy_sEmail(sEmail);
        System.out.println("Email: " + sEmail);
        if(person != null && person.checkPassword(sPassword)) {
            return person;
        } else {
            return new Person();
        }
    }
}
