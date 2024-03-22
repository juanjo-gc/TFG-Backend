package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.OperationRepository;
import es.uca.tfg.backend.rest.OperationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class OperationController {

    @Autowired
    private OperationRepository _operationRepository;

    @Autowired
    private AdminRepository _adminRepository;

    @PostMapping("/newOperation")
    public Operation newOperation(@RequestBody OperationDTO operationDTO) {
        Optional<Admin> optionalAdmin = _adminRepository.findById(operationDTO.get_iAdminId());
        if(optionalAdmin.isPresent()) {
            return _operationRepository.save(new Operation(operationDTO.get_sInformation(), optionalAdmin.get()));
        } else {
            return new Operation();
        }
    }

    @GetMapping("/getOperations/{pageNumber}")
    public Page<Operation> getOperations(@PathVariable("pageNumber") int iPageNumber) {
        return _operationRepository.findAll(PageRequest.of(iPageNumber, 20));
    }
}
