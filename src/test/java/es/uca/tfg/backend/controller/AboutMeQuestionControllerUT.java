package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.repository.AboutMeAnswerRepository;
import es.uca.tfg.backend.repository.AboutMeQuestionRepository;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.OperationRepository;
import es.uca.tfg.backend.rest.AboutMeQuestionAnswerDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class AboutMeQuestionControllerUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private AboutMeQuestionRepository _questionRepository;
    @Mock
    private AboutMeAnswerRepository _answerRepository;
    @Mock
    private AdminRepository _adminRepository;
    @Mock
    private OperationRepository _operationRepository;

    @InjectMocks
    private AboutMeQuestionController controller;

    private AboutMeQuestion _question = new AboutMeQuestion("Example question");
    private Admin _admin = new Admin("admin@admin.com", "password", "admin", "Admin");

    @Test
    public void newAboutMeQuestionWillCreateQuestion() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("Example question", "", 1, -1, -1, -1);
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(_admin));
        Mockito.when(_questionRepository.findById(1)).thenReturn(Optional.of(_question));
        Mockito.when(_questionRepository.save(any(AboutMeQuestion.class))).thenReturn(_question);
        //when
        AboutMeQuestion question = controller.newAboutMeQuestion(dto);
        //then
        Mockito.verify(_operationRepository).save(any(Operation.class));
        Mockito.verify(_questionRepository).save(any(AboutMeQuestion.class));
        Assertions.assertEquals(question.get_sQuestion(), "Example question");
    }

    @Test
    public void newAboutMeQuestionWillFail() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("Example question", "", 1, -1, -1, -1);
        //when
        AboutMeQuestion question = controller.newAboutMeQuestion(dto);
        //then
        Assertions.assertNull(question.get_sQuestion());
    }

    @Test
    public void updateAboutMeQuestionWillUpdateQuestion() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("Example question updated", "", 1, -1, 1, -1);
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(_admin));
        Mockito.when(_questionRepository.findById(1)).thenReturn(Optional.of(_question));
        Mockito.when(_questionRepository.save(any(AboutMeQuestion.class))).thenReturn(_question);
        //when
        AboutMeQuestion question = controller.updateAboutMeQuestion(dto);
        //then
        Mockito.verify(_questionRepository).save(any(AboutMeQuestion.class));
        Mockito.verify(_operationRepository).save(any(Operation.class));
        Assertions.assertEquals(question.get_sQuestion(), "Example question updated");
    }


    @Test
    public void updateAboutMeQuestionWillFail() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("Example question", "", -1, -1, 1, -1);
        //when
        AboutMeQuestion question = controller.updateAboutMeQuestion(dto);
        //then
        Assertions.assertNull(question.get_sQuestion());
    }

    @Test
    public void deleteAboutMeQuestionWillDeleteQuestion() {
        //given
        List<AboutMeAnswer> aAnswersToQuestion = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aAnswersToQuestion.add(new AboutMeAnswer());
        Mockito.when(_questionRepository.findById(1)).thenReturn(Optional.of(_question));
        Mockito.when(_answerRepository.findBy_question(_question)).thenReturn(aAnswersToQuestion);
        //when
        controller.deleteAboutMeQuestion(1);
        //then
        Mockito.verify(_answerRepository, Mockito.times(5)).delete(any(AboutMeAnswer.class));
        Mockito.verify(_questionRepository).deleteById(1);
    }
}
