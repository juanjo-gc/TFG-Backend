package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.repository.InterestRepository;
import es.uca.tfg.backend.rest.InterestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class InterestController {

    @Autowired
    InterestRepository _interestRepository;

    @GetMapping("/getAllInterests")
    public List<Interest> getAllInterests() {
        return _interestRepository.findAll();
    }

    @GetMapping("/getAllInterestNames")
    public List<String> getInterests() {
        return _interestRepository.findInterestNames();
    }

    @PostMapping("/createInterest")
    public Interest createInterest(@RequestBody InterestDTO interestDTO) {
        return _interestRepository.save(new Interest(interestDTO.get_sName()));
    }

    @PatchMapping("/updateInterest/{id}")
    public Interest updateInterest(@RequestBody InterestDTO interestDTO, @PathVariable("id") int iInterestId) {
        Optional<Interest> optionalInterest = _interestRepository.findById(iInterestId);
        if(optionalInterest.isPresent()) {
            Interest interest = optionalInterest.get();
            interest.set_sName(interestDTO.get_sName());
            return _interestRepository.save(interest);
        } else {
            return new Interest();
        }
    }

    @DeleteMapping("/deleteInterest/{id}")
    public void deleteInterest(@PathVariable("id") int iInterestId) {
        _interestRepository.deleteById(iInterestId);
    }
}
