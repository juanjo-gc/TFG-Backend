package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.AboutMeQuestionAnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class AboutMeAnswerController {

    @Autowired
    private AboutMeAnswerRepository _answerRepository;
    @Autowired
    private AboutMeQuestionRepository _questionRepository;
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private OperationRepository _operationRepository;

    @PostMapping("/createOrModifyAboutMeAnswer")
    public boolean newAboutMeAnswer(@RequestBody AboutMeQuestionAnswerDTO answerDTO) {
        //public AboutMeQuestionAnswerDTO(String sQuestion, String sAnswer, int iAdminId, int iUserId, int iQuestionId, int iAnswerId) {
        Optional<User> optionalUser = _userRepository.findById(answerDTO.get_iUserId());
        Optional<AboutMeQuestion> optionalQuestion = _questionRepository.findById(answerDTO.get_iQuestionId());
        System.out.println("Presente?" + optionalQuestion.isPresent());
        if(optionalUser.isPresent() && optionalQuestion.isPresent()) {
            Optional<AboutMeAnswer> optionalAnswer = _answerRepository.findByUserAndQuestion(optionalUser.get(), optionalQuestion.get());
            if(optionalAnswer.isPresent()) {
                //System.out.println("Antes: " +answerDTO.get_sAnswer());
                optionalAnswer.get().set_sAnswer(answerDTO.get_sAnswer());
                //System.out.println("Despues: " + answerDTO.get_sAnswer());
                AboutMeAnswer answer = _answerRepository.save(optionalAnswer.get());
                return true;
            } else {
                 _answerRepository.save(new AboutMeAnswer(answerDTO.get_sAnswer(), optionalQuestion.get(), optionalUser.get()));
                 return true;
            }
        } else {
            return false;
        }
    }

    @PatchMapping("/updateAboutMeAnswer")
    public AboutMeAnswer updateAboutMeAnswer(@RequestBody AboutMeQuestionAnswerDTO answerDTO) {
        Optional<AboutMeAnswer> optionalAnswer = _answerRepository.findById(answerDTO.get_iAnswerId());
        if(optionalAnswer.isPresent()) {
            optionalAnswer.get().set_sAnswer(answerDTO.get_sAnswer());
            return _answerRepository.save(optionalAnswer.get());
        } else {
            return new AboutMeAnswer();
        }
    }

    @PatchMapping("softDeleteAboutMeAnswer/{answerId}")
    public void softDeleteRestoreAboutMeAnswer(@PathVariable("answerId") int iAnswerId) {
        Optional<AboutMeAnswer> optionalAnswer = _answerRepository.findById(iAnswerId);
        if(optionalAnswer.isPresent()) {
            optionalAnswer.get().set_sAnswer("");
            _answerRepository.save(optionalAnswer.get());
        }
    }

    @GetMapping("/getUserAnswers/{userId}")
    public List<AboutMeAnswer> getUserAnswers(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            return _answerRepository.findBy_user(optionalUser.get());
        } else {
            return Collections.emptyList();
        }
    }


}
