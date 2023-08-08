package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class InterestController {

    @Autowired
    InterestRepository _interestRepository;

    @GetMapping("/getAllInterestNames")
    public List<String> getInterests() {
        return _interestRepository.findInterestNames();
    }
}
