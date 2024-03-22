package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.repository.AboutMeAnswerRepository;
import es.uca.tfg.backend.repository.AboutMeQuestionRepository;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.OperationRepository;
import es.uca.tfg.backend.rest.AboutMeQuestionAnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class AboutMeQuestionController {

    @Autowired
    private AboutMeQuestionRepository _questionRepository;
    @Autowired
    private AboutMeAnswerRepository _answerRepository;
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private OperationRepository _operationRepository;


    @PostMapping("/newAboutMeQuestion")
    public AboutMeQuestion newAboutMeQuestion(@RequestBody AboutMeQuestionAnswerDTO questionDTO) {
        Optional<Admin> optionalAdmin = _adminRepository.findById(questionDTO.get_iAdminId());
        if(optionalAdmin.isPresent()) {
            _operationRepository.save(new Operation("Se ha creado una nueva pregunta para los usuarios con texto: " + questionDTO.get_sQuestion(), optionalAdmin.get()));
            return _questionRepository.save(new AboutMeQuestion(questionDTO.get_sQuestion()));
        } else {
            return new AboutMeQuestion();
        }
    }

    @PatchMapping("/updateAboutMeQuestion")
    public AboutMeQuestion updateAboutMeQuestion(@RequestBody AboutMeQuestionAnswerDTO questionDTO) {
        Optional<Admin> optionalAdmin = _adminRepository.findById(questionDTO.get_iAdminId());
        Optional<AboutMeQuestion> optionalQuestion = _questionRepository.findById(questionDTO.get_iQuestionId());
        if(optionalAdmin.isPresent()) {
            optionalQuestion.get().set_sQuestion(questionDTO.get_sQuestion());
            AboutMeQuestion question = _questionRepository.save(optionalQuestion.get());
            if(question.get_iId() != 0)
                _operationRepository.save(new Operation("Se ha modificado la pregunta con ID " + questionDTO.get_iQuestionId() + ". Antes de modificar: Pregunta -> " +
                        optionalQuestion.get().get_sQuestion() + " Después de modificar: Pregunta -> " + questionDTO.get_sQuestion(), optionalAdmin.get()));
            return question;
        } else {
            return new AboutMeQuestion();
        }
    }

    @GetMapping("/getAllAboutMeQuestions")
    public List<AboutMeQuestion> getAllQuestions() {
        return _questionRepository.findAll();
    }

    @DeleteMapping("/deleteAboutMeQuestion/{questionId}")
    public void deleteAboutMeQuestion(@PathVariable("questionId") int iQuestionId) {
        Optional<AboutMeQuestion> optionalQuestion = _questionRepository.findById(iQuestionId);
        if(optionalQuestion.isPresent()) {
            List<AboutMeAnswer> answersToQuestion = _answerRepository.findBy_question(optionalQuestion.get());
            for(AboutMeAnswer answer: answersToQuestion) {
                _answerRepository.delete(answer);
            }
            _questionRepository.deleteById(iQuestionId);
        }
    }

}
