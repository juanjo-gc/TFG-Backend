package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.FAQ;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.FAQRepository;
import es.uca.tfg.backend.repository.OperationRepository;
import es.uca.tfg.backend.rest.FaqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FAQController {

    @Autowired
    private FAQRepository _faqRepository;
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private OperationRepository _operationRepository;

    @PostMapping("/newFAQ/{adminId}")
    public FAQ newFaq(@RequestBody FaqDTO faqDTO, @PathVariable("adminId") int iAdminId) {
        Optional<Admin> optionalAdmin = _adminRepository.findById(iAdminId);
        if(optionalAdmin.isPresent()) {
            FAQ faq = _faqRepository.save(new FAQ(faqDTO.get_sQuestion(), faqDTO.get_sAnswer()));
            _operationRepository.save(new Operation("Se ha creado una nueva FAQ con id " + faq.get_iId() + ".", optionalAdmin.get()));
            return faq;
        } else {
            return new FAQ();
        }
    }

    @PatchMapping("/updateFAQ/{faqId}")
    public FAQ editFaq(@RequestBody FaqDTO faqDTO, @PathVariable("faqId") int iFaqId) {
        Optional<FAQ> optionalFAQ = _faqRepository.findById(iFaqId);
        if(optionalFAQ.isPresent()) {
            FAQ faq = optionalFAQ.get();
            faq.set_sQuestion(faqDTO.get_sQuestion());
            faq.set_sAnswer(faqDTO.get_sAnswer());
            return _faqRepository.save(faq);
        } else {
            return new FAQ();
        }
    }

    @GetMapping("/getAllFAQs")
    public List<FAQ> getAllFAQs() {
        return _faqRepository.findAll();
    }

    @DeleteMapping("/deleteFAQ/{faqId}")
    public void deleteFAQ(@PathVariable("faqId") int iFaqId) {
        _faqRepository.deleteById(iFaqId);
    }
}
