package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class AboutMeAnswerControllerUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Mock
    private AboutMeAnswerRepository _answerRepository;
    @Mock
    private AboutMeQuestionRepository _questionRepository;
    @Mock
    private AdminRepository _adminRepository;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private OperationRepository _operationRepository;
    @InjectMocks
    private AboutMeAnswerController _controller;

    private User _user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
    private AboutMeQuestion _question = new AboutMeQuestion("Question example");
    private AboutMeAnswer _answer = new AboutMeAnswer("Answer example", _question, _user);
    @Test
    public void createOrModifyAboutMeAnswerWillCreateNewAnswer() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("", "Answer to question example", -1, 1, 1, -1);
        Mockito.when(_userRepository.findById(anyInt())).thenReturn(Optional.of(_user));
        Mockito.when(_questionRepository.findById(1)).thenReturn(Optional.of(_question));
        //when
        boolean bIsSuccessful = _controller.newAboutMeAnswer(dto);
        //then
        //Assertions.assertTrue(bIsSuccessful);
        Mockito.verify(_answerRepository).save(any(AboutMeAnswer.class));
    }

    @Test
    public void updateAboutMeAnswerWillUpdateAnswer() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("", "Answer to question example", -1, -1, -1, 1);
        Mockito.when(_answerRepository.findById(1)).thenReturn(Optional.of(_answer));
        Mockito.when(_answerRepository.save(_answer)).thenReturn(_answer);
        //when
        AboutMeAnswer answer = _controller.updateAboutMeAnswer(dto);
        //then
        Mockito.verify(_answerRepository).save(any(AboutMeAnswer.class));
    }

    @Test
    public void updateAboutMeAnswerWillFail() {
        //given
        AboutMeQuestionAnswerDTO dto = new AboutMeQuestionAnswerDTO("", "Answer to question example", -1, -1, -1, 1);
        //when
        AboutMeAnswer answer = _controller.updateAboutMeAnswer(dto);
        //then
        Assertions.assertEquals(answer.get_iId(), 0);
    }

    @Test
    public void deleteAboutMeAnswerWillSoftDeleteExistingAnswer() {
        //given
        Mockito.when(_answerRepository.findById(1)).thenReturn(Optional.of(_answer));
        //when
        _controller.softDeleteRestoreAboutMeAnswer(1);
        //then
        Mockito.verify(_answerRepository).save(any(AboutMeAnswer.class));
    }

    @Test
    public void getUserAnswersWhenExistingUserWillReturnAnswersList() {
        //given
        List<AboutMeAnswer> aUserAnswers = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aUserAnswers.add(new AboutMeAnswer());
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_answerRepository.findBy_user(any(User.class))).thenReturn(aUserAnswers);
        //when
        List<AboutMeAnswer> aAnswers = _controller.getUserAnswers(1);
        //then
        Mockito.verify(_answerRepository).findBy_user(any(User.class));
        Assertions.assertEquals(5, aAnswers.size());
    }

    @Test
    public void getUserAnswersWhenNonExistingUserWillReturnEmptyList() {
        //given
        List<AboutMeAnswer> aUserAnswers = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aUserAnswers.add(new AboutMeAnswer());
        Mockito.when(_answerRepository.findBy_user(any(User.class))).thenReturn(aUserAnswers);
        //when
        List<AboutMeAnswer> aAnswers = _controller.getUserAnswers(1);
        //then
        Mockito.verify(_answerRepository, Mockito.times(0)).findBy_user(any(User.class));
        Assertions.assertEquals(0, aAnswers.size());
    }
}
